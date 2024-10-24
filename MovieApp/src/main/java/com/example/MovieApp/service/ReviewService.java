package com.example.MovieApp.service;

import com.example.MovieApp.entity.Review;
import com.example.MovieApp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Zwraca wszystkie recenzje
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Zwraca recenzję po ID
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // Tworzy nową recenzję
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    // Aktualizuje recenzję
    public Review updateReview(Long id, Review reviewDetails) {
        return reviewRepository.findById(id).map(review -> {
            review.setRating(reviewDetails.getRating());
            review.setComment(reviewDetails.getComment());
            return reviewRepository.save(review);
        }).orElseThrow(() -> new RuntimeException("Review not found with id " + id));
    }

    // Usuwa recenzję
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}

