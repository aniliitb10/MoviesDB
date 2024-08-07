package learning.streamlit.moviedb.entity;

import jakarta.persistence.*;
import learning.streamlit.moviedb.dto.SuperHeroDto;
import learning.streamlit.moviedb.enums.Operation;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "super_hero")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SuperHeroPK.class)
public class SuperHeroEntity {

    @Id
    private String superHeroId;

    @Id
    private Integer version;

    private String name;

    private String imdbLink;

    @Enumerated(EnumType.STRING)
    private Operation operation;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "superhero_awards", joinColumns = {
            @JoinColumn(name = "superHeroId"),
            @JoinColumn(name = "version")
    })
    @Column(name = "award", nullable = false)
    private Set<String> awards;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SuperHeroEntity that)) return false;
        return Objects.equals(getSuperHeroId(), that.getSuperHeroId()) && Objects.equals(getVersion(), that.getVersion()) && Objects.equals(getName(), that.getName()) && Objects.equals(getImdbLink(), that.getImdbLink()) && getOperation() == that.getOperation() && Objects.equals(getAwards(), that.getAwards());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSuperHeroId(), getVersion(), getName(), getImdbLink(), getOperation(), getAwards());
    }

    public SuperHeroDto toDto() {
        return new SuperHeroDto(
                this.superHeroId,
                this.name,
                this.imdbLink,
                this.awards
        );
    }

    public SuperHeroEntity nextVersion(Operation operation) {
        return new SuperHeroEntity(
                this.superHeroId,
                this.version + 1,
                this.getName(),
                this.imdbLink,
                operation,
                this.getAwards()
        );
    }

    public static SuperHeroEntity fromDto(SuperHeroDto dto, Integer version, Operation operation) {
        return new SuperHeroEntity(
                dto.getSuperHeroId(),
                version,
                dto.getName(),
                dto.getImdbLink(),
                operation,
                dto.getAwards()
        );
    }
}