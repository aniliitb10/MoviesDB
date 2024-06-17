package learning.streamlit.moviedb.dto;

import learning.streamlit.moviedb.entity.MovieEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieEntityList {

    private List<MovieEntity> movieEntities;
}
