package com.example.MovieApp.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WatchedMovieKey implements Serializable {

    private Long userId;
    private Long movieId;

    // Konstruktor domyślny
    public WatchedMovieKey() {}

    // Konstruktor z parametrami
    public WatchedMovieKey(Long userId, Long movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    // Gettery i settery
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    // Równość i hashCode dla klucza złożonego
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchedMovieKey that = (WatchedMovieKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(movieId, that.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, movieId);
    }
}
