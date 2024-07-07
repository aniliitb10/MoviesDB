package learning.streamlit.moviedb.service;

import learning.streamlit.moviedb.entity.MovieEntity;
import learning.streamlit.moviedb.enums.Operation;
import learning.streamlit.moviedb.exception.InternalException;
import learning.streamlit.moviedb.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieAuditService movieAuditService;

    public MovieService(MovieRepository movieRepository, MovieAuditService movieAuditService) {
        this.movieRepository = movieRepository;
        this.movieAuditService = movieAuditService;
    }

    public List<MovieEntity> getAllMovies() {
        return movieRepository.findAll();
    }

    @Transactional(rollbackFor = InternalException.class)
    public List<MovieEntity> updateExistingEntries(List<MovieEntity> movies) {
        for (MovieEntity movie : movies) {
            this.movieAuditService.addAnotherVersion(movie, Operation.Update.toString());
        }
        return this.movieRepository.saveAll(movies);
    }

    @Transactional(rollbackFor = InternalException.class)
    public List<MovieEntity> addNewEntries(List<MovieEntity> movies) {
        for (MovieEntity movie : movies) {
            this.movieAuditService.addNewEntity(movie);
        }
        return this.movieRepository.saveAll(movies);
    }

    @Transactional(rollbackFor = InternalException.class)
    public void deleteMoviesById(List<Long> ids) {
        this.movieAuditService.markDeleted(ids);
        this.movieRepository.deleteAllById(ids);
    }
}
