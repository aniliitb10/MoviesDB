package learning.streamlit.moviedb.controller;

import learning.streamlit.moviedb.dto.MovieEntityList;
import learning.streamlit.moviedb.entity.MovieEntity;
import learning.streamlit.moviedb.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies/")
public class MovieController {

    static Logger logger = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping()
    public ResponseEntity<List<MovieEntity>> getAllMovies() {
        var movies = this.movieService.getAllMovies();
        logger.info("Fetched [{}] movies", movies.size());
        return ResponseEntity.ok(movies);
    }

    @PostMapping()
    public ResponseEntity<List<MovieEntity>> update(@RequestBody MovieEntityList movieEntityList) {
        var movies = this.movieService.save(movieEntityList.getMovieEntities());
        logger.info("Updated [{}] movies", movies.size());
        return ResponseEntity.ok(movies);
    }

    @DeleteMapping()
    public void delete(@RequestBody List<Long> movieIds) {
        logger.info("Deleted [{}] movies", movieIds.size());
        this.movieService.deleteMoviesById(movieIds);
    }
}
