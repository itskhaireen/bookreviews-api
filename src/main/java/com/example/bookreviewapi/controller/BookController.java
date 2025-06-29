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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Operation(summary = "Create a new book", description = "Creates a new book with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book created successfully",
            content = @Content(schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid book data"),
        @ApiResponse(responseCode = "409", description = "Book already exists"),
        @ApiResponse(responseCode = "503", description = "Database operation failed")
    })
    public ResponseEntity<BookDTO> addBook(
            @Parameter(description = "Book details", required = true)
            @RequestBody @Valid CreateBookDTO createBookDTO) {
        Book savedBook = bookService.saveBook(BookMapper.toEntity(createBookDTO));
        return ResponseEntity.ok(BookMapper.toDTO(savedBook));
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves a list of all books")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
            content = @Content(schema = @Schema(implementation = BookDTO.class)))
    })
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> bookDTO = bookService.getAllBooks().stream()
            .map(BookMapper::toDTO)
            .toList();
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves a specific book by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book found",
            content = @Content(schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookDTO> getBookById(
            @Parameter(description = "Book ID", required = true)
            @PathVariable Long id) {
        Book book = bookService.getBookByIdOrThrow(id);
        BookDTO dto = BookMapper.toDTO(book);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", description = "Deletes a book by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book ID", required = true)
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/average-rating")
    @Operation(summary = "Get average rating", description = "Calculates and returns the average rating for a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Average rating calculated successfully",
            content = @Content(schema = @Schema(type = "number", format = "double"))),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Double> getAverageRating(
            @Parameter(description = "Book ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getAverageRating(id));
    }
}