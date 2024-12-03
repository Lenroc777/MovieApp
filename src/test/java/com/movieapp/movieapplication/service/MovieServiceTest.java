package com.movieapp.movieapplication.service;
import com.movieapp.movieapplication.model.Category;
import com.movieapp.movieapplication.model.Language;


import com.movieapp.movieapplication.model.Movie;
import com.movieapp.movieapplication.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    private MovieService movieService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private LanguageService languageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movieService = new MovieService(movieRepository, categoryService, languageService, null);
    }

    @Test
    void testGetAllMovies() {
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<Movie> movies = movieService.getAllMovies();
        assertEquals(1, movies.size());
        assertEquals("Test Movie", movies.get(0).getTitle());

        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testGetMovieById_Found() {
        Movie movie = new Movie();
        movie.setId("1");
        movie.setTitle("Test Movie");
        when(movieRepository.findById("1")).thenReturn(java.util.Optional.of(movie));

        var result = movieService.getMovieById("1");

        assertEquals(true, result.isPresent());
        assertEquals("Test Movie", result.get().getTitle());
        verify(movieRepository, times(1)).findById("1");
    }

    @Test
    void testGetMovieById_NotFound() {
        when(movieRepository.findById("1")).thenReturn(java.util.Optional.empty());

        var result = movieService.getMovieById("1");

        assertEquals(false, result.isPresent());
        verify(movieRepository, times(1)).findById("1");
    }

    @Test
    void testAddMovie() {
        // Przygotowanie obiektu Movie
        Movie movie = new Movie();
        movie.setTitle("New Movie");
        movie.setGenres(List.of("Genre1", "Genre2")); // Ustawione ID kategorii
        movie.setLanguageId("Language1");

        // Mockowanie zwracanych wartości przez CategoryService
        Category category1 = new Category("Genre1", "Action", "Action movies");
        Category category2 = new Category("Genre2", "Drama", "Dramatic movies");
        when(categoryService.getCategoryById("Genre1")).thenReturn(java.util.Optional.of(category1));
        when(categoryService.getCategoryById("Genre2")).thenReturn(java.util.Optional.of(category2));

        // Mockowanie zwracanej wartości przez LanguageService
        Language mockLanguage = new Language("Language1", "English", "EN");
        when(languageService.getLanguageById("Language1")).thenReturn(java.util.Optional.of(mockLanguage));

        // Mockowanie zapisu w repozytorium
        when(movieRepository.save(any(Movie.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Wykonanie testowanej metody
        Movie result = movieService.addMovie(movie);

        // Assercje
        assertEquals("New Movie", result.getTitle());
        assertEquals(List.of("Genre1", "Genre2"), result.getGenres());
        assertEquals("Language1", result.getLanguageId());
        verify(movieRepository, times(1)).save(movie);
        verify(categoryService, times(1)).getCategoryById("Genre1");
        verify(categoryService, times(1)).getCategoryById("Genre2");
        verify(languageService, times(1)).getLanguageById("Language1");
    }




    @Test
    void testDeleteMovie() {
        String movieId = "1";

        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).deleteById(movieId);
    }


    @Test
    void testUpdateMovie_NotFound() {
        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("New Title");

        when(movieRepository.findById("1")).thenReturn(java.util.Optional.empty());

        Exception exception = null;
        try {
            movieService.updateMovie("1", updatedMovie);
        } catch (RuntimeException e) {
            exception = e;
        }

        assertEquals("Movie not found with id: 1", exception.getMessage());
        verify(movieRepository, times(1)).findById("1");
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testSaveAll() {
        List<Movie> movies = List.of(
                new Movie("Title1", "Description1", "2023", List.of("Action"), "EN", "Director1", 120, List.of()),
                new Movie("Title2", "Description2", "2022", List.of("Drama"), "EN", "Director2", 90, List.of())
        );

        movieService.saveAll(movies);

        verify(movieRepository, times(1)).saveAll(movies);
    }
}