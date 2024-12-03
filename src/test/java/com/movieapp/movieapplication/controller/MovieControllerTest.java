package com.movieapp.movieapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapp.movieapplication.model.Language;
import com.movieapp.movieapplication.model.Movie;
import com.movieapp.movieapplication.service.LanguageService;
import com.movieapp.movieapplication.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import com.movieapp.movieapplication.model.Category;
import com.movieapp.movieapplication.service.CategoryService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private LanguageService languageService;

    @MockBean
    private MovieService movieService;

    private Movie testMovie;

    @BeforeEach
    void setUp() {
        testMovie = new Movie();
        testMovie.setId("123");
        testMovie.setTitle("Test Movie");
        testMovie.setDescription("Test Description");
        testMovie.setReleaseYear("2023");
        testMovie.setGenres(List.of("Action", "Drama"));
        testMovie.setLanguageId("EN");
        testMovie.setDirector("Test Director");
        testMovie.setDuration(120);

        // Mockowanie CategoryService
        when(categoryService.getCategoryById("Action")).thenReturn(
                Optional.of(new Category("Action", "Action Movies", "Description of action movies"))
        );
        when(categoryService.getCategoryById("Drama")).thenReturn(
                Optional.of(new Category("Drama", "Drama Movies", "Description of drama movies"))
        );

        // Mockowanie LanguageService
        when(languageService.getLanguageById("EN")).thenReturn(
                Optional.of(new Language("EN", "English", "en"))
        );
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllMovies() throws Exception {
        when(movieService.getAllMovies()).thenReturn(List.of(testMovie));

        mockMvc.perform(get("/api/movies/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Movie"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAddMovie() throws Exception {
        when(movieService.addMovie(any(Movie.class))).thenReturn(testMovie);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Movie"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteMovie() throws Exception {
        doNothing().when(movieService).deleteMovie("123");

        mockMvc.perform(delete("/api/movies/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateMovie() throws Exception {
        Movie updatedMovie = new Movie();
        updatedMovie.setId("123");
        updatedMovie.setTitle("Updated Title");
        updatedMovie.setDescription("Updated Description");
        updatedMovie.setReleaseYear("2024");
        updatedMovie.setGenres(List.of("Drama"));
        updatedMovie.setLanguageId("EN");
        updatedMovie.setDirector("Updated Director");
        updatedMovie.setDuration(150);

        when(movieService.getMovieById("123")).thenReturn(Optional.of(testMovie));
        when(movieService.updateMovie(eq("123"), any(Movie.class))).thenReturn(updatedMovie);

        mockMvc.perform(put("/api/movies/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedMovie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

}