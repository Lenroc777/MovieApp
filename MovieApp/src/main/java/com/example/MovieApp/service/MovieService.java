package com.example.MovieApp.service;

import com.example.MovieApp.entity.Movie;
import com.example.MovieApp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // Zwraca wszystkie filmy
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Zwraca film po ID
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    // Tworzy nowy film
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Aktualizuje film
    public Movie updateMovie(Long id, Movie movieDetails) {
        return movieRepository.findById(id).map(movie -> {
            movie.setTitle(movieDetails.getTitle());
            movie.setDescription(movieDetails.getDescription());
            movie.setReleaseDate(movieDetails.getReleaseDate());
            return movieRepository.save(movie);
        }).orElseThrow(() -> new RuntimeException("Movie not found with id " + id));
    }

    // Usuwa film
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
