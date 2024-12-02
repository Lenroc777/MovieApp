package com.movieapp.movieapplication.repository;

import com.movieapp.movieapplication.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
}
