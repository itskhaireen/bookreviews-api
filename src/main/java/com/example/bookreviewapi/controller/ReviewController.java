package com.example.bookreviewapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import com.example.bookreviewapi.service.ReviewService;

import jakarta.validation.Valid;

import com.example.bookreviewapi.mapper.ReviewMapper;
import com.example.bookreviewapi.dto.ReviewDTO;
import com.example.bookreviewapi.dto.CreateReviewDTO;
import com.example.bookreviewapi.model.Review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/api/books/{bookId}/reviews")
@Tag(name = "Review Management", description = "APIs for managing book reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @Operation(
        summary = "Add a review to a book",
        description = "Creates a new review for a specific book. The rating must be between 1-5, and all fields are required."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review created successfully",
            content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid review data provided"),
        @ApiResponse(responseCode = "404", description = "Book not found with the given ID")
    })
    public ResponseEntity<ReviewDTO> addReview(
        @Parameter(description = "ID of the book to add review to", required = true)
        @PathVariable Long bookId,
        @Parameter(description = "Review details to create", required = true)
        @RequestBody @Valid CreateReviewDTO createReviewDTO) {
        Review savedReview = reviewService.saveReview(bookId, ReviewMapper.toEntity(createReviewDTO));
        return ResponseEntity.ok(ReviewMapper.toDTO(savedReview));
    }

    @GetMapping
    @Operation(
        summary = "Get all reviews for a book",
        description = "Retrieves all reviews associated with a specific book."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully",
            content = @Content(schema = @Schema(implementation = ReviewDTO.class))),
        @ApiResponse(responseCode = "404", description = "Book not found with the given ID")
    })
    public ResponseEntity<List<ReviewDTO>> getReviewsByBookId(
        @Parameter(description = "ID of the book to get reviews for", required = true)
        @PathVariable Long bookId) {
        List<ReviewDTO> reviewDTO = reviewService.getReviewsByBookId(bookId).stream()
            .map(ReviewMapper::toDTO)
            .toList();
        return ResponseEntity.ok(reviewDTO);
    }
}