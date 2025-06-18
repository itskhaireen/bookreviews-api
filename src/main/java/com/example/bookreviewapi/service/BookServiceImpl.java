package com.example.bookreviewapi.service;

import org.springframework.stereotype.Service;
import com.example.bookreviewapi.repository.BookRepository;
import com.example.bookreviewapi.exception.BookNotFoundException;
import com.example.bookreviewapi.model.Book;
import com.example.bookreviewapi.model.Review;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book getBookByIdOrThrow(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public double getAverageRating(Long bookId) {

        Book bookRating = getBookByIdOrThrow(bookId);
        List<Review> reviews = bookRating.getReviews();

        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }

        // Calculate the average rating
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .sum();

        return averageRating / reviews.size();
       
    }
}