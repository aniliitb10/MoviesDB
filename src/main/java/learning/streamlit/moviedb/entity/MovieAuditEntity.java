package learning.streamlit.moviedb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class MovieAuditEntity {

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    private MovieAuditEntityPK key;

    private String title;
    private Integer year;
    private Integer votes;
    private float rating;
    private String genres;
    private String operation;

    public MovieAuditEntity(Long id, Long version, String title, Integer year,  Integer votes, float rating,
                            String genres, String operation) {
        this.key = new MovieAuditEntityPK(id, version);
        this.title = title;
        this.year = year;
        this.votes = votes;
        this.rating = rating;
        this.genres = genres;
        this.operation = operation;
    }

    public Long getId() {
        return this.key.getId();
    }

    public Long getVersion() {
        return this.key.getVersion();
    }

    public static MovieAuditEntity fromMovieEntity(MovieEntity movie, String operation, Long version) {
        return new MovieAuditEntity(
                movie.getId(),
                version,
                movie.getTitle(),
                movie.getYear(),
                movie.getVotes(),
                movie.getRating(),
                movie.getGenres(),
                operation
                );
    }

    public static MovieAuditEntity nextVersion(MovieAuditEntity movieAuditEntity) {
        return new MovieAuditEntity(
                movieAuditEntity.getId(),
                movieAuditEntity.getVersion() + 1,
                movieAuditEntity.title,
                movieAuditEntity.year,
                movieAuditEntity.votes,
                movieAuditEntity.rating,
                movieAuditEntity.genres,
                movieAuditEntity.operation
        );
    }
}
