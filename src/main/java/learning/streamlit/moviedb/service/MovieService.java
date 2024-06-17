package learning.streamlit.moviedb.service;

import learning.streamlit.moviedb.entity.MovieEntity;
import learning.streamlit.moviedb.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieEntity> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<MovieEntity> save(List<MovieEntity> movies) {
        return this.movieRepository.saveAll(movies);
    }

    public void deleteMoviesById(List<Long> ids) {
        this.movieRepository.deleteAllById(ids);
    }
}
