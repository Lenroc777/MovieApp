package com.movieapp.movieapplication.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "movies")
public class Movie {
    @Id
    private String id;
    private String title;
    private String description;
    private String releaseYear;
    private List<String> genres; // Lista ID kategorii
    private String languageId; // Referencja do ID języka
    private String director;
    private int duration;

    public Movie(String title, String description, String releaseYear, List<String> genres, String languageId, String director, int duration, List<String> reviewIds) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genres = genres;
        this.languageId = languageId;
        this.director = director;
        this.duration = duration;

    }
    public Movie() {}
    // Gettery i settery
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }




}
