package com.example.bookreviewapi.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import com.example.bookreviewapi.service.BookService;

import jakarta.validation.Valid;

import com.example.bookreviewapi.mapper.BookMapper;
import com.example.bookreviewapi.dto.BookDTO;
import com.example.bookreviewapi.dto.CreateBookDTO;
import com.example.bookreviewapi.model.Book;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Implement the validation logic in the CreateBookDTO.java
    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody @Valid CreateBookDTO createBookDTO) {
        Book savedBook = bookService.saveBook(BookMapper.toEntity(createBookDTO)); // --- No instance required (static method). Called the class name directly.
        return ResponseEntity.ok(BookMapper.toDTO(savedBook)); // -- Returning the saved book as a DTO.
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> bookDTO = bookService.getAllBooks().stream()
            .map(BookMapper::toDTO)
            .toList();
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookByIdOrThrow(id);
        BookDTO dto = BookMapper.toDTO(book);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getAverageRating(id));
    }
    
}