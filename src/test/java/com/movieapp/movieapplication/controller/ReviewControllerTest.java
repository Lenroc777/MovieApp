package com.movieapp.movieapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieapp.movieapplication.model.Review;
import com.movieapp.movieapplication.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReviewController reviewController = new ReviewController(reviewService);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule()); // Rejestracja modułu
    }


    @Test
    void testGetAllReviews() throws Exception {
        Review review1 = new Review("1", "movie1", "user1", 5, "Great movie!", LocalDateTime.now());
        Review review2 = new Review("2", "movie2", "user2", 4, "Good movie!", LocalDateTime.now());

        when(reviewService.getAllReviews()).thenReturn(List.of(review1, review2));

        mockMvc.perform(get("/api/reviews/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value("movie1"))
                .andExpect(jsonPath("$[1].movieId").value("movie2"));

        verify(reviewService, times(1)).getAllReviews();
    }

    @Test
    void testGetReviewById_Found() throws Exception {
        String reviewId = "1";
        Review review = new Review(reviewId, "movie1", "user1", 5, "Great movie!", LocalDateTime.now());

        when(reviewService.getReviewById(reviewId)).thenReturn(Optional.of(review));

        mockMvc.perform(get("/api/reviews/" + reviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value("movie1"));

        verify(reviewService, times(1)).getReviewById(reviewId);
    }

    @Test
    void testGetReviewById_NotFound() throws Exception {
        String reviewId = "1";

        when(reviewService.getReviewById(reviewId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reviews/" + reviewId))
                .andExpect(status().isNotFound());

        verify(reviewService, times(1)).getReviewById(reviewId);
    }

    @Test
    void testAddReview() throws Exception {
        Review review = new Review("1", "movie1", "user1", 5, "Great movie!", LocalDateTime.now());

        when(reviewService.addReview(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieId").value("movie1"))
                .andExpect(jsonPath("$.rating").value(5));

        verify(reviewService, times(1)).addReview(any(Review.class));
    }

    @Test
    void testUpdateReview_Found() throws Exception {
        String reviewId = "1";
        Review updatedReview = new Review(reviewId, "movie1", "user1", 5, "Amazing movie!", LocalDateTime.now());

        when(reviewService.updateReview(eq(reviewId), any(Review.class))).thenReturn(updatedReview);

        mockMvc.perform(put("/api/reviews/" + reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReview)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewText").value("Amazing movie!"));

        verify(reviewService, times(1)).updateReview(eq(reviewId), any(Review.class));
    }

    @Test
    void testDeleteReview() throws Exception {
        String reviewId = "1";

        doNothing().when(reviewService).deleteReview(reviewId);

        mockMvc.perform(delete("/api/reviews/" + reviewId))
                .andExpect(status().isNoContent());

        verify(reviewService, times(1)).deleteReview(reviewId);
    }
}