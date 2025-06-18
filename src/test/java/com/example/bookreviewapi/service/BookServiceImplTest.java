package com.example.bookreviewapi.service;

import com.example.bookreviewapi.exception.BookNotFoundException;
import com.example.bookreviewapi.model.Book;
import com.example.bookreviewapi.model.Review;
import com.example.bookreviewapi.repository.BookRepository;
import com.example.bookreviewapi.repository.ReviewRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    void getAverageRating_whenBookExistsWithReviews_shouldReturnCorrectAverage() {
        // Arrange
        Long bookId = 1L;
        
        // Create reviews
        Review review1 = new Review();
        review1.setId(1L);
        review1.setRating(4);
        review1.setReviewer("John");
        review1.setComment("Great book!");
        
        Review review2 = new Review();
        review2.setId(2L);
        review2.setRating(5);
        review2.setReviewer("Jane");
        review2.setComment("Excellent!");
        
        Review review3 = new Review();
        review3.setId(3L);
        review3.setRating(3);
        review3.setReviewer("Bob");
        review3.setComment("Good book");
        
        // Create book with reviews
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setReviews(List.of(review1, review2, review3));
        
        // Mock the repository to return our book
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        
        // Act
        double result = bookService.getAverageRating(bookId);
        
        // Assert
        // Expected: (4 + 5 + 3) / 3 = 12 / 3 = 4.0
        assertEquals(4.0, result, 0.01);
        
        // Verify that getBookByIdOrThrow was called (indirectly through repository)
        verify(bookRepository, times(1)).findById(bookId);
    }
    
    @Test
    void getAverageRating_whenBookExistsButNoReviews_shouldReturnZero() {
        // Arrange
        Long bookId = 1L;
        
        // Create book with empty reviews list
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setReviews(List.of()); // Empty list
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        
        // Act
        double result = bookService.getAverageRating(bookId);
        
        // Assert
        assertEquals(0.0, result, 0.01);
        
        // Verify repository was called
        verify(bookRepository, times(1)).findById(bookId);
    }
    
    @Test
    void getAverageRating_whenBookExistsButReviewsIsNull_shouldReturnZero() {
        // Arrange
        Long bookId = 1L;
        
        // Create book with null reviews
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setReviews(null); // Null reviews
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        
        // Act
        double result = bookService.getAverageRating(bookId);
        
        // Assert
        assertEquals(0.0, result, 0.01);
        
        // Verify repository was called
        verify(bookRepository, times(1)).findById(bookId);
    }
    
    @Test
    void getAverageRating_whenBookDoesNotExist_shouldThrowBookNotFoundException() {
        // Arrange
        Long bookId = 999L;
        
        // Mock repository to return empty (book not found)
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookService.getAverageRating(bookId));
        
        // Verify repository was called
        verify(bookRepository, times(1)).findById(bookId);
    }
    
    @Test
    void getAverageRating_whenBookHasSingleReview_shouldReturnThatRating() {
        // Arrange
        Long bookId = 1L;
        
        // Create single review
        Review review = new Review();
        review.setId(1L);
        review.setRating(5);
        review.setReviewer("Single Reviewer");
        review.setComment("Amazing book!");
        
        // Create book with single review
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setReviews(List.of(review));
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        
        // Act
        double result = bookService.getAverageRating(bookId);
        
        // Assert
        assertEquals(5.0, result, 0.01);
        
        // Verify repository was called
        verify(bookRepository, times(1)).findById(bookId);
    }
    
    @Test
    void getAverageRating_whenBookHasReviewsWithDecimalResult_shouldReturnCorrectAverage() {
        // Arrange
        Long bookId = 1L;
        
        // Create reviews that will result in decimal average
        Review review1 = new Review();
        review1.setId(1L);
        review1.setRating(3);
        
        Review review2 = new Review();
        review2.setId(2L);
        review2.setRating(4);
        
        Review review3 = new Review();
        review3.setId(3L);
        review3.setRating(5);
        
        Review review4 = new Review();
        review4.setId(4L);
        review4.setRating(2);
        
        // Create book with reviews
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setReviews(List.of(review1, review2, review3, review4));
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        
        // Act
        double result = bookService.getAverageRating(bookId);
        
        // Assert
        // Expected: (3 + 4 + 5 + 2) / 4 = 14 / 4 = 3.5
        assertEquals(3.5, result, 0.01);
        
        // Verify repository was called
        verify(bookRepository, times(1)).findById(bookId);
    }
}