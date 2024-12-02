package com.movieapp.movieapplication.repository;

import com.movieapp.movieapplication.model.Language;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LanguageRepository extends MongoRepository<Language, String> {
}
