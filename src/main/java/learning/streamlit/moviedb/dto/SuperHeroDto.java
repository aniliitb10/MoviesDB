package learning.streamlit.moviedb.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SuperHeroDto {
    private String superHeroId;
    private String name;
    private String imdbLink;
    private Set<String> awards;
}
