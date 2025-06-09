package com.example.bookreviewapi.repository;

import com.example.bookreviewapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    
}

// This interface extends JpaRepository, which provides CRUD operations for the Book entity.
// It allows you to perform operations like saving, deleting, and finding books without needing to implement these methods manually.
// The Long type parameter indicates that the ID of the Book entity is of type Long.
// You can use this repository in your service layer to interact with the database for Book entities.
// You can also define custom query methods here if needed, such as finding books by title or author. 