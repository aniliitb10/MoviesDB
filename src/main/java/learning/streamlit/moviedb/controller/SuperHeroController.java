package learning.streamlit.moviedb.controller;

import learning.streamlit.moviedb.dto.SuperHeroDto;
import learning.streamlit.moviedb.dto.SuperHeroDtoList;
import learning.streamlit.moviedb.entity.SuperHeroEntity;
import learning.streamlit.moviedb.service.SuperHeroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/superhero/")
public class SuperHeroController {
    static final Logger logger = LoggerFactory.getLogger(SuperHeroController.class);
    private final SuperHeroService superHeroService;

    public SuperHeroController(SuperHeroService superHeroService) {
        this.superHeroService = superHeroService;
    }

    @GetMapping("/all/")
    public ResponseEntity<List<SuperHeroEntity>> getAll() {
        return ResponseEntity.ok(superHeroService.findAll());
    }

    @GetMapping("/all/{superHeroId}/")
    public ResponseEntity<List<SuperHeroEntity>> getAll( @PathVariable String superHeroId) {
        return ResponseEntity.ok(superHeroService.findAllBySuperHeroId(superHeroId));
    }

    @GetMapping
    public ResponseEntity<List<SuperHeroDto>> getLatestAndActiveSuperHeroes() {
        var entities = this.superHeroService.findLatestAndNotDeletedDtoList();
        logger.info("Found [{}] active superheroes (only latest versions)", entities.size());
        return ResponseEntity.ok(entities);
    }

    @PutMapping
    public ResponseEntity<List<SuperHeroDto>> updateAll(@RequestBody SuperHeroDtoList superHeroDtoList) {
        var dtoList = this.superHeroService.update(superHeroDtoList.getSuperHeroDtoList());
        logger.info("Updated [{}] SuperHeros", dtoList.size());
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping
    public ResponseEntity<List<SuperHeroDto>> create(@RequestBody SuperHeroDtoList superHeroDtoList) {
        var dtoList = this.superHeroService.addNew(superHeroDtoList.getSuperHeroDtoList());
        logger.info("Created [{}] SuperHeros", dtoList.size());
        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping
    public void delete(@RequestBody List<String> superHeroIdList) {
        this.superHeroService.delete(superHeroIdList);
        logger.info("Deleted [{}] SuperHeros", superHeroIdList.size());
    }
}
