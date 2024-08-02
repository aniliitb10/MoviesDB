package learning.streamlit.moviedb.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class MovieDto {
    public MovieDto(Long id, String title, Integer year, Integer votes, float rating, String genres) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.votes = votes;
        this.rating = rating;
        this.genres = genres;
    }

    private Long id;
    private String title;
    private Integer year;
    private Integer votes;
    private Float rating;
    private String genres;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDto movieDto = (MovieDto) o;
        return Objects.equals(id, movieDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
