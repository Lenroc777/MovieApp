package com.example.MovieApp.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WatchedMovies")
public class WatchedMovie {

    @EmbeddedId
    private WatchedMovieKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movieId")
    private Movie movie;

    @Column(name = "watchedAt", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date watchedAt;

    // Gettery i Settery
    public WatchedMovieKey getId() {
        return id;
    }

    public void setId(WatchedMovieKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Date getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(Date watchedAt) {
        this.watchedAt = watchedAt;
    }
}
