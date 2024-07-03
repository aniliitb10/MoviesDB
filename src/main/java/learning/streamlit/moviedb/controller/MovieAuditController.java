package learning.streamlit.moviedb.controller;

import learning.streamlit.moviedb.entity.MovieAuditEntity;
import learning.streamlit.moviedb.repository.MovieAuditRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movie_audit")
public class MovieAuditController {

    private final MovieAuditRepository movieAuditRepository;

    public MovieAuditController(MovieAuditRepository movieAuditRepository) {
        this.movieAuditRepository = movieAuditRepository;
    }

    @GetMapping("/{movieId}/")
    ResponseEntity<List<MovieAuditEntity>> get_movie_version(@PathVariable Long movieId) {
        return ResponseEntity.ok(this.movieAuditRepository.findAllByKeyId(movieId));
    }
}
