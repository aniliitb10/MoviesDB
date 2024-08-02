package learning.streamlit.moviedb.service;

import learning.streamlit.moviedb.dto.MovieDto;
import learning.streamlit.moviedb.entity.MovieEntity;
import learning.streamlit.moviedb.enums.Operation;
import learning.streamlit.moviedb.exception.InternalException;
import learning.streamlit.moviedb.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieDto> getAllMovies() {
        return this.movieRepository.findLatestVersionOfAllIds().stream().
                filter(m -> !Objects.equals(m.getOperation(), Operation.Delete.toString())).
                map(MovieEntity::toMovieDto).
                toList();
    }

    public List<MovieEntity> getAllVersions(Long movieId) {
        return this.movieRepository.findAllByKeyId(movieId);
    }

    public List<MovieEntity> findAllEntries() {
        return this.movieRepository.findAll();
    }

    private static void assertValidEntity(MovieEntity movieEntity, Long movieId) {
        if (movieEntity == null || Operation.valueOf(movieEntity.getOperation()) == Operation.Delete) {
            String error = String.format("Movie Id [%d] doesn't exist or has been Deleted in Audit", movieId);
            logger.error(error);
            throw new InternalException(error);
        }
    }

    @Transactional(rollbackFor = InternalException.class)
    public MovieEntity addAnotherVersion(MovieDto movie) {
        // this could be used for both Update and Delete operations, but not New!
        var movieAudit = this.movieRepository.findFirstByKeyIdOrderByKeyVersionDesc(movie.getId());
        assertValidEntity(movieAudit, movie.getId());

        var nextAudit = MovieEntity.fromMovieDto(movie, Operation.Update.toString(), movieAudit.getVersion() + 1L);
        return this.movieRepository.save(nextAudit);
    }

    @Transactional(rollbackFor = InternalException.class)
    public MovieEntity addNewEntity(MovieDto movie) {
        // this can only be used for New operation, neither Update nor Delete operations!
        // hence, there can't be any other existing entity (except of Deleted operation)
        var movieAudit = this.movieRepository.findFirstByKeyIdOrderByKeyVersionDesc(movie.getId());

        if (movieAudit != null && Operation.valueOf(movieAudit.getOperation()) != Operation.Delete) {
            var err = String.format("Movie Id [%d] already exists and is not deleted", movie.getId());
            logger.error(err);
            throw new InternalException(err);
        }

        final Long version = (movieAudit != null) ? movieAudit.getVersion() + 1 : 1L;
        MovieEntity movieEntity = MovieEntity.fromMovieDto(movie, Operation.New.toString(), version);
        return this.movieRepository.save(movieEntity);
    }

    @Transactional(rollbackFor = InternalException.class)
    public List<MovieDto> updateExistingEntries(List<MovieDto> movieDtoList) {
        return movieDtoList.stream().
                map(this::addAnotherVersion).
                map(MovieEntity::toMovieDto).
                toList();
    }

    @Transactional(rollbackFor = InternalException.class)
    public List<MovieDto> addNewEntries(List<MovieDto> movieDtoList) {
        return movieDtoList.stream().
                map(this::addNewEntity).
                map(MovieEntity::toMovieDto).
                toList();
    }

    @Transactional(rollbackFor = InternalException.class)
    public void markDeleted(List<Long> movieIds) {
        for (Long movieId : movieIds) {
            MovieEntity latestAudit = this.movieRepository.findFirstByKeyIdOrderByKeyVersionDesc(movieId);
            assertValidEntity(latestAudit, movieId);

            MovieEntity nextAuditVersion = latestAudit.nextVersion(Operation.Delete);
            this.movieRepository.save(nextAuditVersion);
        }
    }
}
