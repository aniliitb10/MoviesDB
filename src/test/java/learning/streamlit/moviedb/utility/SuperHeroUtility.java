package learning.streamlit.moviedb.utility;

import learning.streamlit.moviedb.dto.SuperHeroDto;
import learning.streamlit.moviedb.entity.SuperHeroEntity;

import java.util.Objects;

public class SuperHeroUtility {

    // this doesn't make sense in the source, hence provided here
    public static boolean isEqual(SuperHeroEntity left, SuperHeroEntity right) {
        if (left == right) return true;
        return Objects.equals(left.getSuperHeroId(), right.getSuperHeroId()) &&
                Objects.equals(left.getVersion(), right.getVersion()) &&
                Objects.equals(left.getName(), right.getName()) &&
                Objects.equals(left.getImdbLink(), right.getImdbLink()) &&
                left.getOperation() == right.getOperation() &&
                Objects.equals(left.getAwards(), right.getAwards());
    }

    public static boolean isEqual(SuperHeroDto left, SuperHeroDto right) {
        if (left == right) return true;
        return Objects.equals(left.getSuperHeroId(), right.getSuperHeroId()) &&
                Objects.equals(left.getName(), right.getName()) &&
                Objects.equals(left.getImdbLink(), right.getImdbLink()) &&
                Objects.equals(left.getAwards(), right.getAwards());
    }
}
