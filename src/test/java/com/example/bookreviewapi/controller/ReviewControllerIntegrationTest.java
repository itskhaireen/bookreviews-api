package com.example.bookreviewapi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads_andMockMvcIsInjected() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void addReview_shouldReturnSavedReview() throws Exception {
        // Step 1: Create a book first
        String bookJson = """
            {
                "title": "Book for Review Test",
                "author": "Review Author",
                "genre": "Review Genre"
            }
            """;

        MvcResult bookResult = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andReturn();

        Long bookId = objectMapper.readTree(bookResult.getResponse().getContentAsString()).get("id").asLong();

        // Step 2: Add a review to the book
        String reviewJson = """
            {
                "reviewer": "Test Reviewer",
                "comment": "This is a great book!",
                "rating": 5
            }
            """;

        mockMvc.perform(post("/api/books/{bookId}/reviews", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reviewer").value("Test Reviewer"))
                .andExpect(jsonPath("$.comment").value("This is a great book!"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    void addReview_whenBookDoesNotExist_shouldReturn404() throws Exception {
        Long nonExistentBookId = 999L;
        
        String reviewJson = """
            {
                "reviewer": "Test Reviewer",
                "comment": "This is a great book!",
                "rating": 5
            }
            """;

        mockMvc.perform(post("/api/books/{bookId}/reviews", nonExistentBookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with ID: " + nonExistentBookId));
    }

    @Test
    void addReview_whenInvalidData_shouldReturn400() throws Exception {
        // Step 1: Create a book first
        String bookJson = """
            {
                "title": "Book for Invalid Review Test",
                "author": "Invalid Review Author",
                "genre": "Invalid Review Genre"
            }
            """;

        MvcResult bookResult = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andReturn();

        Long bookId = objectMapper.readTree(bookResult.getResponse().getContentAsString()).get("id").asLong();

        // Step 2: Try to add invalid review (missing required fields)
        String invalidReviewJson = """
            {
                "reviewer": "",
                "comment": "",
                "rating": 6
            }
            """;

        mockMvc.perform(post("/api/books/{bookId}/reviews", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidReviewJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsByBookId_whenBookHasReviews_shouldReturnReviews() throws Exception {
        // Step 1: Create a book
        String bookJson = """
            {
                "title": "Book for Get Reviews Test",
                "author": "Get Reviews Author",
                "genre": "Get Reviews Genre"
            }
            """;

        MvcResult bookResult = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andReturn();

        Long bookId = objectMapper.readTree(bookResult.getResponse().getContentAsString()).get("id").asLong();

        // Step 2: Add multiple reviews
        String review1Json = """
            {
                "reviewer": "Reviewer 1",
                "comment": "First review",
                "rating": 4
            }
            """;

        String review2Json = """
            {
                "reviewer": "Reviewer 2",
                "comment": "Second review",
                "rating": 5
            }
            """;

        mockMvc.perform(post("/api/books/{bookId}/reviews", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(review1Json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/books/{bookId}/reviews", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(review2Json))
                .andExpect(status().isOk());

        // Step 3: Get all reviews for the book
        mockMvc.perform(get("/api/books/{bookId}/reviews", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].reviewer").value("Reviewer 1"))
                .andExpect(jsonPath("$[0].comment").value("First review"))
                .andExpect(jsonPath("$[0].rating").value(4))
                .andExpect(jsonPath("$[1].reviewer").value("Reviewer 2"))
                .andExpect(jsonPath("$[1].comment").value("Second review"))
                .andExpect(jsonPath("$[1].rating").value(5))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[1].id").isNumber());
    }

    @Test
    void getReviewsByBookId_whenBookHasNoReviews_shouldReturnEmptyArray() throws Exception {
        // Step 1: Create a book
        String bookJson = """
            {
                "title": "Book with No Reviews",
                "author": "No Reviews Author",
                "genre": "No Reviews Genre"
            }
            """;

        MvcResult bookResult = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andReturn();

        Long bookId = objectMapper.readTree(bookResult.getResponse().getContentAsString()).get("id").asLong();

        // Step 2: Get reviews for book with no reviews
        mockMvc.perform(get("/api/books/{bookId}/reviews", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getReviewsByBookId_whenBookDoesNotExist_shouldReturn404() throws Exception {
        Long nonExistentBookId = 999L;

        mockMvc.perform(get("/api/books/{bookId}/reviews", nonExistentBookId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with ID: " + nonExistentBookId));
    }

    @Test
    void addMultipleReviewsAndGetAverageRating_shouldWorkCorrectly() throws Exception {
        // Step 1: Create a book
        String bookJson = """
            {
                "title": "Book for Complex Test",
                "author": "Complex Test Author",
                "genre": "Complex Test Genre"
            }
            """;

        MvcResult bookResult = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andReturn();

        Long bookId = objectMapper.readTree(bookResult.getResponse().getContentAsString()).get("id").asLong();

        // Step 2: Add multiple reviews
        String[] reviewJsons = {
            """
            {
                "reviewer": "Alice",
                "comment": "Great book!",
                "rating": 5
            }
            """,
            """
            {
                "reviewer": "Bob",
                "comment": "Good book",
                "rating": 4
            }
            """,
            """
            {
                "reviewer": "Charlie",
                "comment": "Average book",
                "rating": 3
            }
            """
        };

        for (String reviewJson : reviewJsons) {
            mockMvc.perform(post("/api/books/{bookId}/reviews", bookId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(reviewJson))
                    .andExpect(status().isOk());
        }

        // Step 3: Get all reviews
        mockMvc.perform(get("/api/books/{bookId}/reviews", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(org.hamcrest.Matchers.hasSize(3)));

        // Step 4: Get average rating (should be (5+4+3)/3 = 4.0)
        mockMvc.perform(get("/api/books/{bookId}/average-rating", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(4.0));
    }
} 