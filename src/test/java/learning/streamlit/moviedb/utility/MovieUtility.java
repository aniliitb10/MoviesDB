package learning.streamlit.moviedb.utility;

import learning.streamlit.moviedb.dto.MovieDto;

import java.util.Objects;

public class MovieUtility {
    public static MovieDto getMovieDto(Long id, String title) {
        return new MovieDto(id, title, 2000, 100, 5.6f, "Comedy");
    }

    public static boolean isEqual(MovieDto left, MovieDto right) {
        return Objects.equals(left.getId(), right.getId()) &&
                Objects.equals(left.getTitle(), right.getTitle()) &&
                Objects.equals(left.getYear(), right.getYear()) &&
                Objects.equals(left.getVotes(), right.getVotes()) &&
                Objects.equals(left.getRating(), right.getRating()) &&
                Objects.equals(left.getGenres(), right.getGenres());
    }
}
