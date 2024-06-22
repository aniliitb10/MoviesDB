package learning.streamlit.moviedb.service;

import learning.streamlit.moviedb.entity.MovieAuditEntity;
import learning.streamlit.moviedb.entity.MovieEntity;
import learning.streamlit.moviedb.enums.Operation;
import learning.streamlit.moviedb.repository.MovieAuditRepository;
import learning.streamlit.moviedb.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieAuditService {

    private final MovieAuditRepository movieAuditRepository;
    private final MovieRepository movieRepository;

    public MovieAuditService(MovieAuditRepository movieAuditRepository, MovieRepository movieRepository) {
        this.movieAuditRepository = movieAuditRepository;
        this.movieRepository = movieRepository;
    }

    public Long getLatestVersionNumber(Long movieId) {
        var movieAudit = this.movieAuditRepository.findFirstByKeyIdOrderByKeyVersionDesc(movieId);
        return (movieAudit != null) ? movieAudit.getVersion() : null;
    }

    public void addAnotherVersion(MovieEntity movie, String operation) {
        Long latestVersion = this.getLatestVersionNumber(movie.getId());
        if (latestVersion == null) {
            latestVersion = this.addNewEntity(movie).getVersion();
        }

        MovieAuditEntity movieAuditEntity = MovieAuditEntity.fromMovieEntity(movie, operation, latestVersion + 1);
        this.movieAuditRepository.save(movieAuditEntity);
    }

    public MovieAuditEntity addNewEntity(MovieEntity movie) {
        MovieAuditEntity movieAuditEntity = MovieAuditEntity.fromMovieEntity(movie, Operation.New.toString(), 1L);
        return this.movieAuditRepository.save(movieAuditEntity);
    }

    public void markDeleted(List<Long> movieIds) {
        for (Long movieId : movieIds) {
            MovieAuditEntity latestAudit = this.movieAuditRepository.findFirstByKeyIdOrderByKeyVersionDesc(movieId);
            if (latestAudit == null) {
                var movie = this.movieRepository.findById(movieId);
                latestAudit = addNewEntity(movie.orElseThrow());
            }

            MovieAuditEntity nextAuditVersion = MovieAuditEntity.nextVersion(latestAudit);
            nextAuditVersion.setOperation(Operation.Delete.toString());
            this.movieAuditRepository.save(nextAuditVersion);
        }
    }
}
