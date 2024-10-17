package com.example.MovieApp.repository;

import com.example.MovieApp.entity.WatchedMovie;
import com.example.MovieApp.entity.WatchedMovieKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, WatchedMovieKey> {
}
