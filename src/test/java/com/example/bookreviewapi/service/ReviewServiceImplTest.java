package com.example.bookreviewapi.service;

import com.example.bookreviewapi.exception.BookNotFoundException;
import com.example.bookreviewapi.exception.InvalidReviewDataException;
import com.example.bookreviewapi.exception.DatabaseOperationException;
import com.example.bookreviewapi.model.Book;
import com.example.bookreviewapi.model.Review;
import com.example.bookreviewapi.repository.BookRepository;
import com.example.bookreviewapi.repository.ReviewRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void saveReview_whenValidReviewAndBookExists_shouldSaveSuccessfully() {
        // Arrange
        Long bookId = 1L;
        
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        
        Review inputReview = new Review();
        inputReview.setReviewer("John Doe");
        inputReview.setComment("Great book!");
        inputReview.setRating(5);
        
        Review savedReview = new Review();
        savedReview.setId(1L);
        savedReview.setReviewer("John Doe");
        savedReview.setComment("Great book!");
        savedReview.setRating(5);
        savedReview.setBook(book);
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
        
        // Act
        Review result = reviewService.saveReview(bookId, inputReview);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getReviewer());
        assertEquals("Great book!", result.getComment());
        assertEquals(5, result.getRating());
        assertEquals(book, result.getBook());
        
        // Verify the book relationship was set
        verify(bookRepository, times(1)).findById(bookId);
        verify(reviewRepository, times(1)).save(inputReview);
        
        // Verify the book was set on the review before saving
        assertEquals(book, inputReview.getBook());
    }

    @Test
    void getReviewsByBookId_whenBookExistsWithReviews_shouldReturnReviews() {
        // Arrange
        Long bookId = 1L;
        
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        
        Review review1 = new Review();
        review1.setId(1L);
        review1.setReviewer("John");
        review1.setComment("Great book!");
        review1.setRating(5);
        review1.setBook(book);
        
        Review review2 = new Review();
        review2.setId(2L);
        review2.setReviewer("Jane");
        review2.setComment("Excellent!");
        review2.setRating(4);
        review2.setBook(book);
        
        List<Review> reviews = List.of(review1, review2);
        book.setReviews(reviews);
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        
        // Act
        List<Review> result = reviewService.getReviewsByBookId(bookId);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getReviewer());
        assertEquals("Jane", result.get(1).getReviewer());
        assertEquals(5, result.get(0).getRating());
        assertEquals(4, result.get(1).getRating());
        
        // Verify repository was called
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void getReviewsByBookId_whenBookExistsButNoReviews_shouldReturnEmptyList() {
        // Arrange
        Long bookId = 1L;
        
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setReviews(List.of()); // Empty reviews
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        
        // Act
        List<Review> result = reviewService.getReviewsByBookId(bookId);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verify repository was called
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void getReviewsByBookId_whenBookExistsButReviewsIsNull_shouldReturnNull() {
        // Arrange
        Long bookId = 1L;
        
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setReviews(null); // Null reviews
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        
        // Act
        List<Review> result = reviewService.getReviewsByBookId(bookId);
        
        // Assert
        assertNull(result);
        
        // Verify repository was called
        verify(bookRepository, times(1)).findById(bookId);
    }
} 