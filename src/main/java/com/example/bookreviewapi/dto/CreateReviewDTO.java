package com.example.bookreviewapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateReviewDTO {

    @NotBlank(message = "Comment is required")
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be no more than 5")
    private int rating;

    @NotBlank(message = "Reviewer is required")
    @Size(max = 100, message = "Reviewer name cannot exceed 100 characters")
    private String reviewer;
    
}