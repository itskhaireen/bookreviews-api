package com.example.bookreviewapi.controller;

import com.example.bookreviewapi.dto.CreateReviewDTO;
import com.example.bookreviewapi.dto.ReviewDTO;
import com.example.bookreviewapi.mapper.ReviewMapper;
import com.example.bookreviewapi.model.Review;
import com.example.bookreviewapi.service.ReviewService;

import jakarta.validation.Valid;

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
    public ResponseEntity<ReviewDTO> addReview(@PathVariable Long bookId, @RequestBody @Valid CreateReviewDTO CreateReviewDTO) {
        Review saveReview = reviewService.saveReview(bookId, ReviewMapper.toEntity(CreateReviewDTO));
        return ResponseEntity.ok(ReviewMapper.toDTO(saveReview));
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getReviewsByBookId (@PathVariable Long bookId) {
        List<Review> getReview = reviewService.getReviewsByBookId(bookId);
        List<ReviewDTO> dtos = getReview.stream()
            .map(ReviewMapper::toDTO)
            .toList();
        
        return ResponseEntity.ok(dtos);
    }
    
}