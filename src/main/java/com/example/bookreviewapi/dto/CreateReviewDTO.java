package com.example.bookreviewapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateReviewDTO {

    @NotBlank(message = "Comment is required")
    private String comment;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 2, message = "Rating must be no more than 5")
    private int rating;

    @NotBlank(message = "Reviewer is required")
    @Size(max = 50 )
    private String reviewer;
    
}