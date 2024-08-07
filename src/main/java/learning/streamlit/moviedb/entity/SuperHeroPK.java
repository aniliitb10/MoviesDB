package learning.streamlit.moviedb.entity;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SuperHeroPK implements Serializable {
    private String superHeroId;
    private Integer version;
}
