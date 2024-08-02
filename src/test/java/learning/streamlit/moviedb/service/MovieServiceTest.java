package learning.streamlit.moviedb.service;

import learning.streamlit.moviedb.TestApp;
import learning.streamlit.moviedb.dto.MovieDto;
import learning.streamlit.moviedb.enums.Operation;
import learning.streamlit.moviedb.exception.InternalException;
import learning.streamlit.moviedb.repository.MovieRepository;
import learning.streamlit.moviedb.utility.MovieUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static learning.streamlit.moviedb.utility.MovieUtility.getMovieDto;
import static learning.streamlit.moviedb.utility.MovieUtility.isEqual;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApp.class)
@Profile("test")
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        this.movieRepository.deleteAll();
    }

    /**
     * Verify the count of entries in the db - for a particular movie and all movies
     *
     * @param movieId:      the target movie id whose count id to be verified
     * @param movieIdCount: the expected count of @movieId
     * @param totalCount:   the total count of entries in db
     */
    private void verifyCountOfEntries(Long movieId, Integer movieIdCount, Integer totalCount) {
        assertEquals(this.movieRepository.findAllByKeyId(movieId).size(), movieIdCount);
        assertEquals(this.movieRepository.findAll().size(), totalCount);
    }

    @Test
    void getAllMovies() {
        assertTrue(this.movieService.getAllMovies().isEmpty());
        MovieDto movieDto = getMovieDto(1L, "3 Idiots");
        this.movieService.addNewEntity(movieDto);
        verifyCountOfEntries(1L, 1, 1);

        var allMovies = this.movieService.getAllMovies();
        assertEquals(allMovies.size(), 1);
        var fetchedMovieDto = allMovies.get(0);
        assertTrue(MovieUtility.isEqual(movieDto, fetchedMovieDto));

        this.movieService.addNewEntity(getMovieDto(2L, "Sholay"));
        verifyCountOfEntries(2L, 1, 2);
        assertEquals(this.movieService.getAllMovies().size(), 2);
        assertEquals(this.movieService.getAllMovies().stream().map(MovieDto::getId).collect(Collectors.toSet()),
                Set.of(1L, 2L));

    }

    @Test
    void add() {
        // every testcase will test the add method, let's test the edge cases here
        MovieDto movieDto = getMovieDto(1L, "3 Idiots");
        this.movieService.addNewEntity(movieDto);
        verifyCountOfEntries(1L, 1, 1);
        var allMovies = this.movieService.getAllMovies();
        assertTrue(isEqual(movieDto, allMovies.get(0)));

        // adding the same movie again must fail
        assertThrows(InternalException.class, () -> this.movieService.addNewEntity(movieDto),
                String.format("Movie Id [%d] already exists and is not deleted", movieDto.getId()));
        assertEquals(this.movieService.getAllMovies().size(), 1);
        verifyCountOfEntries(1L, 1, 1);

        // let's try adding another movie
        MovieDto anotherMovieDto = getMovieDto(2L, "Mohabbatein");
        this.movieService.addNewEntity(anotherMovieDto);
        var updatedMovies = this.movieService.getAllMovies();
        verifyCountOfEntries(2L, 1, 2);
        assertTrue(isEqual(updatedMovies.get(1), anotherMovieDto));
    }

    @Test
    void update() {
        MovieDto movieDto = getMovieDto(1L, "3 Idiots");
        this.movieService.addNewEntries(List.of(movieDto));
        assertEquals(this.movieService.getAllMovies().size(), 1);
        verifyCountOfEntries(1L, 1, 1);

        movieDto.setYear(movieDto.getYear() + 1);
        this.movieService.updateExistingEntries(List.of(movieDto));
        assertEquals(this.movieService.getAllMovies().size(), 1);
        verifyCountOfEntries(1L, 2, 2);
        assertEquals(this.movieRepository.findAllByKeyId(movieDto.getId()).get(1).getYear(), movieDto.getYear());

        MovieDto anotherMovieDto = getMovieDto(2L, "Bourne Identity");
        this.movieService.addNewEntries(List.of(anotherMovieDto));
        assertEquals(this.movieService.getAllMovies().size(), 2);
        verifyCountOfEntries(2L, 1, 3);
        verifyCountOfEntries(1L, 2, 3);

        // now delete the first one, it must reduce the movie count as its latest state will be Deleted
        // But log entry should also increase
        this.movieService.markDeleted(List.of(1L));
        assertEquals(this.movieService.getAllMovies().size(), 1);
        verifyCountOfEntries(1L, 3, 4);
        verifyCountOfEntries(2L, 1, 4);

        // now the edge cases, updating a non-existent or deleted entries should throw
        var nonExistingMovieDto = getMovieDto(3L, "Mohabbatein");
        verifyCountOfEntries(nonExistingMovieDto.getId(), 0, 4);
        assertThrows(InternalException.class,
                () -> this.movieService.updateExistingEntries(List.of(nonExistingMovieDto)),
                String.format("Movie Id [%d] doesn't exist or has been Deleted in Audit", nonExistingMovieDto.getId()));

        assertThrows(InternalException.class, () -> this.movieService.updateExistingEntries(List.of(movieDto)),
                String.format("Movie Id [%d] doesn't exist or has been Deleted in Audit", movieDto.getId()));
    }

    @Test
    void delete() {
        MovieDto movieDto = getMovieDto(1L, "3 Idiots");
        this.movieService.addNewEntity(movieDto);
        assertEquals(this.movieService.getAllMovies().size(), 1);
        verifyCountOfEntries(1L, 1, 1);
        assertEquals(this.movieService.getAllMovies().size(), 1);
        assertEquals(this.movieService.findAllEntries().size(), 1);
        assertEquals(this.movieService.findAllEntries().get(0).getOperation(), Operation.New.toString());

        this.movieService.markDeleted(List.of(movieDto.getId()));
        verifyCountOfEntries(1L, 2, 2);
        assertEquals(this.movieService.getAllMovies().size(), 0); // this changed from 1 to 0
        assertEquals(this.movieService.findAllEntries().size(), 2);
        assertEquals(this.movieService.findAllEntries().get(1).getOperation(), Operation.Delete.toString());
    }
}