package learning.streamlit.moviedb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile(value = {"prod", "sqlite"})
public class MovieDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieDbApplication.class, args);
    }

}
