package learning.streamlit.moviedb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import learning.streamlit.moviedb.dto.MovieDto;
import learning.streamlit.moviedb.enums.Operation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class MovieEntity {

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    private MovieEntityPK key;

    private String title;
    private Integer year;
    private Integer votes;
    private float rating;
    private String genres;
    private String operation;

    public MovieEntity(Long id, Long version, String title, Integer year, Integer votes, float rating,
                       String genres, String operation) {
        this.key = new MovieEntityPK(id, version);
        this.title = title;
        this.year = year;
        this.votes = votes;
        this.rating = rating;
        this.genres = genres;
        this.operation = operation;
    }

    public static MovieEntity fromMovieDto(MovieDto movie, String operation, Long version) {
        return new MovieEntity(
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

    public Long getId() {
        return this.key.getId();
    }

    public Long getVersion() {
        return this.key.getVersion();
    }

    public MovieDto toMovieDto() {
        return new MovieDto(
                this.getId(),
                this.getTitle(),
                this.getYear(),
                this.getVotes(),
                this.getRating(),
                this.getGenres()
        );
    }

    public MovieEntity nextVersion(Operation operation) {
        return new MovieEntity(
                this.getId(),
                this.getVersion() + 1,
                this.title,
                this.year,
                this.votes,
                this.rating,
                this.genres,
                operation == null ? this.operation : operation.toString()
        );
    }
}
