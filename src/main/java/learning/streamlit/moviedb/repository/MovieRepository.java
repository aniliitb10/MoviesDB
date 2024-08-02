package learning.streamlit.moviedb.repository;

import learning.streamlit.moviedb.entity.MovieEntity;
import learning.streamlit.moviedb.entity.MovieEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<MovieEntity, MovieEntityPK> {
    MovieEntity findFirstByKeyIdOrderByKeyVersionDesc(Long id);

    @Query(value = """
            SELECT new learning.streamlit.moviedb.entity.\
            MovieEntity(e.key.id, e.key.version, e.title, e.year, e.votes, e.rating, e.genres, e.operation) \
            from MovieEntity e where e.key.version = (SELECT MAX(ev.key.version) FROM MovieEntity ev \
            WHERE ev.key.id = e.key.id)"""
    )
    List<MovieEntity> findLatestVersionOfAllIds();

    List<MovieEntity> findAllByKeyId(Long id);
}
