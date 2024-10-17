package com.example.MovieApp.service;

import com.example.MovieApp.entity.WatchedMovie;
import com.example.MovieApp.entity.WatchedMovieKey;
import com.example.MovieApp.repository.WatchedMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WatchedMovieService {
    @Autowired
    private WatchedMovieRepository watchedMovieRepository;

    // Zwraca wszystkie obejrzane filmy
    public List<WatchedMovie> getAllWatchedMovies() {
        return watchedMovieRepository.findAll();
    }

    // Zwraca obejrzany film po kluczu złożonym
    public Optional<WatchedMovie> getWatchedMovieById(WatchedMovieKey id) {
        return watchedMovieRepository.findById(id);
    }

    // Dodaje nowy obejrzany film
    public WatchedMovie createWatchedMovie(WatchedMovie watchedMovie) {
        return watchedMovieRepository.save(watchedMovie);
    }

    // Usuwa obejrzany film
    public void deleteWatchedMovie(WatchedMovieKey id) {
        watchedMovieRepository.deleteById(id);
    }
}
