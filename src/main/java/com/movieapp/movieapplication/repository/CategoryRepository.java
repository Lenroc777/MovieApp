package com.movieapp.movieapplication.repository;

import com.movieapp.movieapplication.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
