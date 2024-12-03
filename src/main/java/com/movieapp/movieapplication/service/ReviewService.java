package com.movieapp.movieapplication.service;

import com.movieapp.movieapplication.model.Review;
import com.movieapp.movieapplication.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public void deleteReview(String id) {
        reviewRepository.deleteById(id);
    }

    public Review updateReview(String id, Review review) {
        return reviewRepository.findById(id).map(existingReview -> {
            existingReview.setMovieId(review.getMovieId());
            existingReview.setUserId(review.getUserId());
            existingReview.setRating(review.getRating());
            existingReview.setReviewText(review.getReviewText());
            existingReview.setReviewDate(review.getReviewDate());
            return reviewRepository.save(existingReview);
        }).orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }
}
