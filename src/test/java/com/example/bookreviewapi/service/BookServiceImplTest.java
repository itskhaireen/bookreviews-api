package com.example.bookreviewapi.service;

import com.example.bookreviewapi.exception.BookNotFoundException;
import com.example.bookreviewapi.model.Book;
import com.example.bookreviewapi.model.Review;
import com.example.bookreviewapi.repository.BookRepository;
import com.example.bookreviewapi.repository.ReviewRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock   
    private BookRepository bookRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void saveBook_shouldReturnSavedBook() {
        // Arrange (Prepare data and mock behavior)
        Book inputBook = new Book();
        inputBook.setId(2L);
        inputBook.setTitle("Test Effective Java");
        inputBook.setAuthor("Putri Khairi");

        when(bookRepository.save(any(Book.class))).thenReturn(inputBook);

        // Act (Call the method to be tested)
        Book savedBook = bookService.saveBook(inputBook);

        // Assert (Verify the results)
        assertNotNull(savedBook);
        assertEquals(2L, savedBook.getId());
        assertEquals("Test Effective Java", savedBook.getTitle());
        assertEquals("Putri Khairi", savedBook.getAuthor());


        // This proves service delegated the call to the repository properly.
        verify(bookRepository, times(1)).save(inputBook);
    }

    @Test
    void getBookByIdOrThrow_whenBookExists_shouldReturnBook() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Architecture");
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act (Call the actual method to be tested)
        Book result = bookService.getBookByIdOrThrow(1L);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Clean Architecture", result.getTitle());
    }

    @Test
    void getBookByIdOrThrow_whenBookDoesNotExist_shouldThrowException() {
        // Arrange -- no mock data since the book does not exist
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookService.getBookByIdOrThrow(99L));

        // Verify that the repository was called
        verify(bookRepository, times(1)).findById(99L);

    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        // Arrange and prepare data > 1 since its a list of books
        Book book1 = new Book();
        book1.setId(34L);
        book1.setTitle("Life On Earth");
        book1.setAuthor("Ellis Wang");
        book1.setGenre("Science Fiction");

        Book book2 = new Book();
        book2.setId(23L);
        book2.setTitle("Sherlock Holmes");
        book2.setAuthor("Arthur Conan Doyle");
        book2.setGenre("Classic Literature");

        // Create the list
        List<Book> mockBook = List.of(book1, book2);

        when(bookRepository.findAll()).thenReturn(mockBook);
        
        // Act (Call the actual method to be tested)
        List<Book> result = bookService.getAllBooks();

        // Assert - Compare the size and contents of the list
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Life On Earth", result.get(0).getTitle());
        assertEquals("Sherlock Holmes", result.get(1).getTitle());

        assertEquals("Ellis Wang", result.get(0).getAuthor());
        assertEquals("Arthur Conan Doyle", result.get(1).getAuthor());

        assertEquals("Science Fiction", result.get(0).getGenre());
        assertEquals("Classic Literature", result.get(1).getGenre());

        // Verify that the repository was called once
        verify(bookRepository, times(1)).findAll();

    }

    @Test
    void getAverageRating_shouldReturnAverageOfRatings() {
        // Arrange
        Review review1 = new Review();
        review1.setRating(4);

        Review review2 = new Review();
        review2.setRating(5);

        List<Review> mockReviews = List.of(review1, review2);

        when(reviewRepository.findByBookId(1L)).thenReturn(mockReviews);

        // Act
        double average = bookService.getAverageRating(1L);

        // Assert
        assertEquals(4.5, average);

        verify(reviewRepository, times(1)).findByBookId(1L);
    }

    @Test
    void getAverageRating_shouldReturnZeroWhenNoReviews() {
        // Arrange
        when(reviewRepository.findByBookId(1L)).thenReturn(Collections.emptyList());

        // Act
        double average = bookService.getAverageRating(1L);

        // Assert
        assertEquals(0.0, average);

        verify(reviewRepository, times(1)).findByBookId(1L);
    }
    
}