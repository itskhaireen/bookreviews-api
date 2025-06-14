package com.example.bookreviewapi.mapper;

import com.example.bookreviewapi.dto.CreateReviewDTO;
import com.example.bookreviewapi.dto.ReviewDTO;
import com.example.bookreviewapi.model.Review;

public class ReviewMapper {

    // call a static method directly using the class name, without needing to create an instance of the class.
    
    // Mapped CreateReviewDTO --> Review (Input - POST API)
    // No IDs because it's auto generated in the Review.java (Entity)
    public static Review toEntity(CreateReviewDTO dto) {
        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setReviewer(dto.getReviewer());

        return review;
    }

    // Mapped Review --> ReviewDTO (Output - GET API)
    public static ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId()); // Users need to see the bookId for reviews of a specific book
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setReviewer(review.getReviewer());

        return dto;
    }
    
}