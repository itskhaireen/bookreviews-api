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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @Operation(
        summary = "Create a new book",
        description = "Creates a new book with the provided details. All fields are required and validated."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book created successfully",
            content = @Content(schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid book data provided"),
        @ApiResponse(responseCode = "409", description = "Book with same title and author already exists")
    })
    public ResponseEntity<BookDTO> addBook(
        @Parameter(description = "Book details to create", required = true)
        @RequestBody @Valid CreateBookDTO createBookDTO) {
        Book savedBook = bookService.saveBook(BookMapper.toEntity(createBookDTO));
        return ResponseEntity.ok(BookMapper.toDTO(savedBook));
    }

    @GetMapping
    @Operation(
        summary = "Get all books",
        description = "Retrieves a list of all books in the system with their reviews and average ratings."
    )
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
    @Operation(
        summary = "Get book by ID",
        description = "Retrieves a specific book by its ID, including all associated reviews."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book found and retrieved successfully",
            content = @Content(schema = @Schema(implementation = BookDTO.class))),
        @ApiResponse(responseCode = "404", description = "Book not found with the given ID")
    })
    public ResponseEntity<BookDTO> getBookById(
        @Parameter(description = "ID of the book to retrieve", required = true)
        @PathVariable Long id) {
        Book book = bookService.getBookByIdOrThrow(id);
        BookDTO dto = BookMapper.toDTO(book);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a book",
        description = "Deletes a book and all its associated reviews from the system."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found with the given ID")
    })
    public ResponseEntity<Void> deleteBook(
        @Parameter(description = "ID of the book to delete", required = true)
        @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/average-rating")
    @Operation(
        summary = "Get average rating for a book",
        description = "Calculates and returns the average rating for a specific book based on all its reviews."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Average rating calculated successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found with the given ID")
    })
    public ResponseEntity<Double> getAverageRating(
        @Parameter(description = "ID of the book to get average rating for", required = true)
        @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getAverageRating(id));
    }
}