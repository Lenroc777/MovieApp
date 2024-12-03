package com.movieapp.movieapplication.service;

import com.movieapp.movieapplication.model.Review;
import com.movieapp.movieapplication.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewService = new ReviewService(reviewRepository);
    }

    @Test
    void testGetAllReviews() {
        Review review = new Review("1", "movie1", "user1", 5, "Great movie!", LocalDateTime.now());
        when(reviewRepository.findAll()).thenReturn(List.of(review));

        List<Review> reviews = reviewService.getAllReviews();

        assertEquals(1, reviews.size());
        assertEquals("movie1", reviews.get(0).getMovieId());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testGetReviewById_Found() {
        Review review = new Review("1", "movie1", "user1", 5, "Great movie!", LocalDateTime.now());
        when(reviewRepository.findById("1")).thenReturn(Optional.of(review));

        Optional<Review> result = reviewService.getReviewById("1");

        assertTrue(result.isPresent());
        assertEquals("movie1", result.get().getMovieId());
        verify(reviewRepository, times(1)).findById("1");
    }

    @Test
    void testGetReviewById_NotFound() {
        when(reviewRepository.findById("1")).thenReturn(Optional.empty());

        Optional<Review> result = reviewService.getReviewById("1");

        assertFalse(result.isPresent());
        verify(reviewRepository, times(1)).findById("1");
    }

    @Test
    void testAddReview() {
        Review review = new Review("1", "movie1", "user1", 5, "Great movie!", LocalDateTime.now());
        when(reviewRepository.save(review)).thenReturn(review);

        Review result = reviewService.addReview(review);

        assertEquals("movie1", result.getMovieId());
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testDeleteReview() {
        String reviewId = "1";

        reviewService.deleteReview(reviewId);

        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void testUpdateReview_Found() {
        Review existingReview = new Review("1", "movie1", "user1", 4, "Good movie!", LocalDateTime.now());
        Review updatedReview = new Review("1", "movie1", "user1", 5, "Amazing movie!", LocalDateTime.now());

        when(reviewRepository.findById("1")).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Review result = reviewService.updateReview("1", updatedReview);

        assertEquals(5, result.getRating());
        assertEquals("Amazing movie!", result.getReviewText());
        verify(reviewRepository, times(1)).findById("1");
        verify(reviewRepository, times(1)).save(existingReview);
    }

    @Test
    void testUpdateReview_NotFound() {
        Review updatedReview = new Review("1", "movie1", "user1", 5, "Amazing movie!", LocalDateTime.now());

        when(reviewRepository.findById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            reviewService.updateReview("1", updatedReview);
        });

        assertEquals("Review not found with id: 1", exception.getMessage());
        verify(reviewRepository, times(1)).findById("1");
        verify(reviewRepository, never()).save(any(Review.class));
    }
}