package com.movieapp.movieapplication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapp.movieapplication.model.Movie;
import com.movieapp.movieapplication.service.MovieService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable String id) {
        return movieService.getMovieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.addMovie(movie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable String id, @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.updateMovie(id, movie));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportMovies() throws JsonProcessingException {
        List<Movie> movies = movieService.getAllMovies();
        String json = new ObjectMapper().writeValueAsString(movies);
        byte[] output = json.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDisposition(ContentDisposition.attachment().filename("movies.json").build());

        return new ResponseEntity<>(output, headers, HttpStatus.OK);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importMovies(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("git");
        String json = new String(file.getBytes(), StandardCharsets.UTF_8);
        List<Movie> movies = new ObjectMapper().readValue(json, new TypeReference<List<Movie>>() {});
        movieService.saveAll(movies);

        return ResponseEntity.ok("Movies imported successfully");
    }

}
