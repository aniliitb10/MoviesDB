package learning.streamlit.moviedb.service;

import jakarta.transaction.Transactional;
import learning.streamlit.moviedb.dto.SuperHeroDto;
import learning.streamlit.moviedb.entity.SuperHeroEntity;
import learning.streamlit.moviedb.enums.Operation;
import learning.streamlit.moviedb.exception.InternalException;
import learning.streamlit.moviedb.repository.SuperHeroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuperHeroService {
    private final SuperHeroRepository superHeroRepository;

    protected SuperHeroService(SuperHeroRepository superHeroRepository) {
        this.superHeroRepository = superHeroRepository;
    }

    /*
     * Verify that either the entity doesn't exist
     * or its state should be deleted - so that it can be resurrected
     * As the operation is fixed for a version, just getting the operation of latest version should suffice
     * */
    private void assertIsGoodToAdd(SuperHeroEntity entity) {
        if (entity != null && entity.getOperation() != Operation.Delete) {
            throw new InternalException(String.format("Superhero [{%s}] exists and is not in deleted state",
                    entity.getSuperHeroId()));
        }
    }

    private static void assertIsGoodToUpdate(SuperHeroEntity entity, String superHeroId) {
        if (entity == null || entity.getOperation() == Operation.Delete) {
            throw new InternalException(String.format("Superhero [{%s}] does not exist or is in deleted state",
                    superHeroId));
        }
    }

    private SuperHeroEntity update(SuperHeroDto dto) {
        var entity = this.superHeroRepository.findFirstBySuperHeroIdOrderByVersionDesc(dto.getSuperHeroId());
        assertIsGoodToUpdate(entity, dto.getSuperHeroId());

        int nextVersion = entity.getVersion() + 1;
        var nextVersionEntity = SuperHeroEntity.fromDto(dto, nextVersion, Operation.Update);
        return superHeroRepository.save(nextVersionEntity);
    }

    @Transactional
    public List<SuperHeroDto> update(List<SuperHeroDto> dtoList) {
        return dtoList.stream().map(d -> this.update(d).toDto()).toList();
    }

    private SuperHeroEntity addNew(SuperHeroDto dto) {
        var entity = this.superHeroRepository.findFirstBySuperHeroIdOrderByVersionDesc(dto.getSuperHeroId());
        assertIsGoodToAdd(entity);
        int nextVersion = entity == null ? 1 : entity.getVersion() + 1;
        var nextVersionEntity = SuperHeroEntity.fromDto(dto, nextVersion, Operation.New);
        return superHeroRepository.save(nextVersionEntity);
    }

    @Transactional
    public List<SuperHeroDto> addNew(List<SuperHeroDto> dtoList) {
        return dtoList.stream().map(d -> this.addNew(d).toDto()).toList();
    }

    @Transactional
    public void delete(List<String> superHeroIds) {
        for (String superHeroId : superHeroIds) {
            var entity = this.superHeroRepository.findFirstBySuperHeroIdOrderByVersionDesc(superHeroId);
            assertIsGoodToUpdate(entity, superHeroId);

            var nextVersionEntity = entity.nextVersion(Operation.Delete);
            this.superHeroRepository.save(nextVersionEntity);
        }
    }

    /*
    * This method fetches all entities, and uses stream to ensure that
    * only the entities with latest versions are selected and converted to corresponding dtos
    * */
    public List<SuperHeroDto> findLatestAndNotDeletedDtoList() {
        var entities = superHeroRepository.findAll();
        var entitiesMap = entities.stream().collect(Collectors.toMap(
                SuperHeroEntity::getSuperHeroId,
                s -> s, (existing, replacement) -> existing.getVersion() > replacement.getVersion() ? existing : replacement
        ));

        return entitiesMap.values().stream().
                filter(e -> e.getOperation() != Operation.Delete).
                map(SuperHeroEntity::toDto).
                toList();
    }

    public List<SuperHeroEntity> findAll() {
        return superHeroRepository.findAll();
    }

    public List<SuperHeroEntity> findAllBySuperHeroId(String superHeroId) {
        return superHeroRepository.findAllBySuperHeroId(superHeroId);
    }
}
