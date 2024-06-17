package learning.streamlit.moviedb.controller;

import learning.streamlit.moviedb.dto.MovieEntityList;
import learning.streamlit.moviedb.entity.MovieEntity;
import learning.streamlit.moviedb.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies/")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public List<MovieEntity> index() {
        return this.movieService.getAllMovies();
    }

    @PostMapping("/")
    public List<MovieEntity> update(@RequestBody MovieEntityList movies) {
        return this.movieService.save(movies.getMovieEntities());
    }

    @DeleteMapping("/")
    public void delete(@RequestBody List<Long> movieIds) {
        this.movieService.deleteMoviesById(movieIds);
    }
}
