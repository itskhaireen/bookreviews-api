package com.example.bookreviewapi.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    
    private Long id;
    private String comment;
    private int rating;

}