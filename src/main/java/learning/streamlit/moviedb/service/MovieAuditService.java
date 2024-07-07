package learning.streamlit.moviedb.service;

import learning.streamlit.moviedb.entity.MovieAuditEntity;
import learning.streamlit.moviedb.entity.MovieEntity;
import learning.streamlit.moviedb.enums.Operation;
import learning.streamlit.moviedb.exception.InternalException;
import learning.streamlit.moviedb.repository.MovieAuditRepository;
import learning.streamlit.moviedb.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieAuditService {
    private static final Logger logger = LoggerFactory.getLogger(MovieAuditService.class);

    private final MovieAuditRepository movieAuditRepository;
    private final MovieRepository movieRepository;

    public MovieAuditService(MovieAuditRepository movieAuditRepository, MovieRepository movieRepository) {
        this.movieAuditRepository = movieAuditRepository;
        this.movieRepository = movieRepository;
        // some sanity check, we can make this behavior config driven
        this.validateAudit();
    }

    protected void validateAudit() {
        var movies = movieRepository.findAll();
        for (var movie : movies) {
            var movieAuditEntity = this.movieAuditRepository.findFirstByKeyIdOrderByKeyVersionDesc(movie.getId());
            assertValidAudit(movieAuditEntity, movie.getId());
        }
    }

    private static void assertValidAudit(MovieAuditEntity movieAuditEntity, Long movieId) {
        if (movieAuditEntity == null || Operation.valueOf(movieAuditEntity.getOperation()) == Operation.Delete) {
            String error = String.format("Movie Id [%d] doesn't exist or has been Deleted in Audit", movieId);
            logger.error(error);
            throw new InternalException(error);
        }
    }

    @Transactional(rollbackFor = InternalException.class)
    public void addAnotherVersion(MovieEntity movie, String operation) {
        // this could be used for both Update and Delete operations, but not New!
        var movieAudit = this.movieAuditRepository.findFirstByKeyIdOrderByKeyVersionDesc(movie.getId());
        assertValidAudit(movieAudit, movie.getId());

        MovieAuditEntity movieAuditEntity = MovieAuditEntity.fromMovieEntity(movie, operation, movieAudit.getVersion() + 1);
        this.movieAuditRepository.save(movieAuditEntity);
    }

    @Transactional(rollbackFor = InternalException.class)
    public void addNewEntity(MovieEntity movie) {
        // this can only be used for New operation, neither Update nor Delete operations!
        // hence, there can't be any other existing entity (except of Deleted operation)
        var movieAudit = this.movieAuditRepository.findFirstByKeyIdOrderByKeyVersionDesc(movie.getId());

        if (movieAudit != null && Operation.valueOf(movieAudit.getOperation()) != Operation.Delete) {
            var err = String.format("Audit [%s] for Movie Id [%d] already exists",
                    movieAudit.getOperation(), movie.getId());
            logger.error(err);
            throw new InternalException(err);
        }

        final Long version = (movieAudit != null) ? movieAudit.getVersion() + 1 : 1L;
        MovieAuditEntity movieAuditEntity = MovieAuditEntity.fromMovieEntity(movie, Operation.New.toString(), version);
        this.movieAuditRepository.save(movieAuditEntity);
    }

    @Transactional(rollbackFor = InternalException.class)
    public void markDeleted(List<Long> movieIds) {
        for (Long movieId : movieIds) {
            MovieAuditEntity latestAudit = this.movieAuditRepository.findFirstByKeyIdOrderByKeyVersionDesc(movieId);
            assertValidAudit(latestAudit, movieId);

            MovieAuditEntity nextAuditVersion = MovieAuditEntity.nextVersion(latestAudit);
            nextAuditVersion.setOperation(Operation.Delete.toString());
            this.movieAuditRepository.save(nextAuditVersion);
        }
    }
}
