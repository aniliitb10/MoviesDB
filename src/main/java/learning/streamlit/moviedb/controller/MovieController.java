package learning.streamlit.moviedb.controller;

import learning.streamlit.moviedb.dto.MovieDto;
import learning.streamlit.moviedb.dto.MovieEntityList;
import learning.streamlit.moviedb.entity.MovieEntity;
import learning.streamlit.moviedb.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class MovieController {

    static Logger logger = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    public String redirectView() {
        return "Server is running! Please refer to <a href=\"/swagger-ui.html\">swagger</a> for APIs documentation";
    }

    @GetMapping("movies/")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        var movies = this.movieService.getAllMovies();
        logger.info("Fetched [{}] movies", movies.size());
        return ResponseEntity.ok(movies);
    }

    @GetMapping("movies-audit/")
    public ResponseEntity<List<MovieEntity>> findAllEntries() {
        var movies = this.movieService.findAllEntries();
        logger.info("Fetched [{}] movie entries", movies.size());
        return ResponseEntity.ok(movies);
    }

    @GetMapping("movies-audit/{movieId}/")
    public ResponseEntity<List<MovieEntity>> getAllMovies(@PathVariable Long movieId) {
        var movies = this.movieService.getAllVersions(movieId);
        logger.info("Fetched [{}] versions of movie id [{}]", movies.size(), movieId);
        return ResponseEntity.ok(movies);
    }

    @PostMapping("movies/")
    public ResponseEntity<List<MovieDto>> add(@RequestBody MovieEntityList movieEntityList) {
        var movies = this.movieService.addNewEntries(movieEntityList.getMovieEntities());
        logger.info("Added [{}] movies", movies.size());
        return ResponseEntity.ok(movies);
    }

    @PutMapping("movies/")
    public ResponseEntity<List<MovieDto>> update(@RequestBody MovieEntityList movieEntityList) {
        var movies = this.movieService.updateExistingEntries(movieEntityList.getMovieEntities());
        logger.info("Updated [{}] movies", movies.size());
        return ResponseEntity.ok(movies);
    }

    @DeleteMapping("movies/")
    public void delete(@RequestBody List<Long> movieIds) {
        logger.info("Deleted [{}] movies", movieIds.size());
        this.movieService.markDeleted(movieIds);
    }
}
