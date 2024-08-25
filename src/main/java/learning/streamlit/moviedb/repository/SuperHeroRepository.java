package learning.streamlit.moviedb.repository;

import learning.streamlit.moviedb.entity.SuperHeroEntity;
import learning.streamlit.moviedb.entity.SuperHeroPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuperHeroRepository extends JpaRepository<SuperHeroEntity, SuperHeroPK> {
    SuperHeroEntity findFirstBySuperHeroIdOrderByVersionDesc(String superHeroId);
    List<SuperHeroEntity> findAllBySuperHeroId(String superHeroId);
}
