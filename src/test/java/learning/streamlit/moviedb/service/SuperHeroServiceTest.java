package learning.streamlit.moviedb.service;

import learning.streamlit.moviedb.TestApp;
import learning.streamlit.moviedb.dto.SuperHeroDto;
import learning.streamlit.moviedb.exception.InternalException;
import learning.streamlit.moviedb.repository.SuperHeroRepository;
import learning.streamlit.moviedb.utility.SuperHeroUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestApp.class)
@Profile("test")
public class SuperHeroServiceTest {

    @Autowired
    private SuperHeroRepository superHeroRepository;

    @Autowired
    private SuperHeroService superHeroService;

    @BeforeEach
    void setUp() {
        this.superHeroRepository.deleteAll();
    }

    private void verifyCounts(int totalRows, int activeLatestRows) {
        assertEquals(totalRows, superHeroRepository.count(), "Total rows do not match");
        assertEquals(activeLatestRows, this.superHeroService.findLatestAndNotDeletedDtoList().size(),
                "Active latest rows do not match");
    }

    @Test
    void testNew() {
        verifyCounts(0, 0);
        SuperHeroDto dto = new SuperHeroDto("s1", "Iron Man", "imdb01", Set.of("oscar"));
        List<SuperHeroDto> dtoList = superHeroService.addNew(List.of(dto));
        assertEquals(dtoList.size(), 1);
        assertTrue(SuperHeroUtility.isEqual(dto, dtoList.get(0)));
        verifyCounts(1, 1);

        // Adding same dto throws
        assertThrows(InternalException.class, () -> superHeroService.addNew(List.of(dto)),
                String.format("Superhero [{%s}] exists and is not in deleted state", dto.getSuperHeroId()));
        verifyCounts(1, 1);

        // Adding the changed dto throws as well -> to save a changed dto, call update method
        dto.setName(dto.getName() + "new name");
        assertThrows(InternalException.class, () -> superHeroService.addNew(List.of(dto)),
                String.format("Superhero [{%s}] exists and is not in deleted state", dto.getSuperHeroId()));
        verifyCounts(1, 1);

        // but adding another item will succeed
        SuperHeroDto dto2 = new SuperHeroDto("s2", "Bat Man", "imdb02", Set.of("oscar", "emmy"));
        List<SuperHeroDto> dtoList2 = superHeroService.addNew(List.of(dto2));
        assertEquals(dtoList2.size(), 1);
        assertTrue(SuperHeroUtility.isEqual(dto2, dtoList2.get(0)));
        verifyCounts(2, 2);
    }

    @Test
    void testUpdate() {
        verifyCounts(0, 0);
        SuperHeroDto dto = new SuperHeroDto("s1", "Iron Man", "imdb01", Set.of("oscar"));
        List<SuperHeroDto> dtoList = superHeroService.addNew(List.of(dto));
        assertEquals(dtoList.size(), 1);
        assertTrue(SuperHeroUtility.isEqual(dto, dtoList.get(0)));
        verifyCounts(1, 1);

        // now, modify the last dto
        dto.setName("Better Iron Man");
        List<SuperHeroDto> dtoList2 = superHeroService.update(List.of(dto));
        assertEquals(dtoList2.size(), 1);
        assertTrue(SuperHeroUtility.isEqual(dto, dtoList2.get(0)));
        verifyCounts(2, 1);
    }
}
