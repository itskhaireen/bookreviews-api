package com.example.bookreviewapi.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String reviewer;
    private String comment;
    private int rating; // Rating out of 5

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}