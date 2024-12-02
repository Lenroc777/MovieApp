package com.movieapp.movieapplication.repository;

import com.movieapp.movieapplication.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
}
