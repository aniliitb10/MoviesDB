package learning.streamlit.moviedb.repository;

import learning.streamlit.moviedb.entity.MovieAuditEntity;
import learning.streamlit.moviedb.entity.MovieAuditEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieAuditRepository extends JpaRepository<MovieAuditEntity, MovieAuditEntityPK> {
    MovieAuditEntity findFirstByKeyIdOrderByKeyVersionDesc(Long id);
}
