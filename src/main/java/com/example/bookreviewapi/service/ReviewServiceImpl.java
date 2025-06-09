package com.example.bookreviewapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.bookreviewapi.repository.BookRepository;
import com.example.bookreviewapi.repository.ReviewRepository;
import com.example.bookreviewapi.model.Review;
import com.example.bookreviewapi.exception.BookNotFoundException;
import com.example.bookreviewapi.model.Book;

@Service
public class ReviewServiceImpl implements ReviewService {
    
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(BookRepository bookRepository, ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review saveReview(Long bookId, Review review) {
        
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        review.setBook(book);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByBookId(Long bookId) {

       Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
       return book.getReviews();
    }
        
}