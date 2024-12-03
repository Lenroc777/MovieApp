package com.movieapp.movieapplication.service;

import com.movieapp.movieapplication.model.Category;
import com.movieapp.movieapplication.model.Movie;
import com.movieapp.movieapplication.model.Review;
import com.movieapp.movieapplication.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final CategoryService categoryService; // Serwis dla kategorii
    private final LanguageService languageService; // Serwis dla języków
    private final ReviewService reviewService; // Serwis dla recenzji

    public MovieService(MovieRepository movieRepository, CategoryService categoryService, LanguageService languageService, ReviewService reviewService) {
        this.movieRepository = movieRepository;
        this.categoryService = categoryService;
        this.languageService = languageService;
        this.reviewService = reviewService;
    }


    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(String id) {
        return movieRepository.findById(id);
    }

    public Movie addMovie(Movie movie) {
        // Walidacja ID kategorii i konwersja na obiekty Category
        List<Category> categories = movie.getGenres().stream()
                .map(categoryId -> categoryService.getCategoryById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId)))
                .toList();

        // Walidacja ID języka
        languageService.getLanguageById(movie.getLanguageId())
                .orElseThrow(() -> new RuntimeException("Language not found: " + movie.getLanguageId()));



        // Zamiana kategorii i recenzji na listy ID
        movie.setGenres(categories.stream().map(Category::getId).toList());

        return movieRepository.save(movie);
    }

    public void deleteMovie(String id) {
        movieRepository.deleteById(id);
    }

    public Movie updateMovie(String id, Movie movie) {
        return movieRepository.findById(id).map(existingMovie -> {
            // Walidacja i aktualizacja ID kategorii
            List<Category> categories = movie.getGenres().stream()
                    .map(categoryId -> categoryService.getCategoryById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId)))
                    .toList();

            // Walidacja i aktualizacja ID języka
            languageService.getLanguageById(movie.getLanguageId())
                    .orElseThrow(() -> new RuntimeException("Language not found: " + movie.getLanguageId()));



            // Aktualizacja pól filmu
            existingMovie.setTitle(movie.getTitle());
            existingMovie.setDescription(movie.getDescription());
            existingMovie.setReleaseYear(movie.getReleaseYear());
            existingMovie.setGenres(categories.stream().map(Category::getId).toList());
            existingMovie.setLanguageId(movie.getLanguageId());
            existingMovie.setDirector(movie.getDirector());
            existingMovie.setDuration(movie.getDuration());

            return movieRepository.save(existingMovie);
        }).orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }
    public void saveAll(List<Movie> movies) {
        movieRepository.saveAll(movies);
    }
}
