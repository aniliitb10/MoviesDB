package learning.streamlit.moviedb.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieDtoList {

    private List<MovieDto> movieEntities;
}
