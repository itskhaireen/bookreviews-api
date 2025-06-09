package com.example.bookreviewapi.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long bookId) {
        super("Book not found with ID: " + bookId);
    }
    
}