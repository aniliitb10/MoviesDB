package learning.streamlit.moviedb.repository;

import learning.streamlit.moviedb.entity.MovieAuditEntity;
import learning.streamlit.moviedb.entity.MovieAuditEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieAuditRepository extends JpaRepository<MovieAuditEntity, MovieAuditEntityPK> {
    MovieAuditEntity findFirstByKeyIdOrderByKeyVersionDesc(Long id);

    List<MovieAuditEntity> findAllByKeyId(Long id);
}
