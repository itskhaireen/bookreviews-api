package com.example.bookreviewapi.controller;

import com.example.bookreviewapi.model.Review;
import com.example.bookreviewapi.service.ReviewService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/books/{bookId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> addReview(@PathVariable Long bookId, @RequestBody Review review) {
        return ResponseEntity.ok(reviewService.saveReview(bookId, review));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviewsByBookId (@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBookId(bookId));
    }
    
}