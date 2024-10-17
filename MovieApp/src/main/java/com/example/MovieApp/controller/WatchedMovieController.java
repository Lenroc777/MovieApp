package com.example.MovieApp.controller;

import com.example.MovieApp.entity.WatchedMovie;
import com.example.MovieApp.entity.WatchedMovieKey;
import com.example.MovieApp.service.WatchedMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watched")
public class WatchedMovieController {

    @Autowired
    private WatchedMovieService watchedMovieService;

    // GET: Zwraca wszystkie obejrzane filmy
    @GetMapping
    public List<WatchedMovie> getAllWatchedMovies() {
        return watchedMovieService.getAllWatchedMovies();
    }

    // GET: Zwraca obejrzany film po ID (kluczu złożonym)
    @GetMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<WatchedMovie> getWatchedMovieById(@PathVariable Long userId, @PathVariable Long movieId) {
        WatchedMovieKey id = new WatchedMovieKey(userId, movieId);
        return watchedMovieService.getWatchedMovieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Dodaje obejrzany film
    @PostMapping
    public WatchedMovie createWatchedMovie(@RequestBody WatchedMovie watchedMovie) {
        return watchedMovieService.createWatchedMovie(watchedMovie);
    }

    // DELETE: Usuwa obejrzany film po ID (kluczu złożonym)
    @DeleteMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<Void> deleteWatchedMovie(@PathVariable Long userId, @PathVariable Long movieId) {
        WatchedMovieKey id = new WatchedMovieKey(userId, movieId);
        watchedMovieService.deleteWatchedMovie(id);
        return ResponseEntity.noContent().build();
    }
}
