package learning.streamlit.moviedb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Getter
@Setter
public class MovieEntity {
    @Id
    private Long id;
    private String title;
    private Integer year;
    private Integer votes;
    private float rating;
    private String genres;
}
