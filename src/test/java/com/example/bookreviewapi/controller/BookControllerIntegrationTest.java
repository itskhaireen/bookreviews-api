package com.example.bookreviewapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
// This class is used for integration testing of the BookController

@Transactional
class BookControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads_andMockMvcIsInjected() {
        // This test just verifies that the Spring context loads and MockMvc is available
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void createBook_shouldReturnSavedBook() throws Exception {
        // Arrange: Prepare a JSON request body
        String bookJson = """
        {
          \"title\": \"Integration Test Book\",
          \"author\": \"Test Author\",
          \"genre\": \"Test Genre\"
        }
        """;

        // Act & Assert: Perform POST request and check response
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Integration Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.genre").value("Test Genre"))
                .andExpect(jsonPath("$.id").isNumber()); // ID should be generated
    }

    @Test
    void getBooks_shouldReturnListOfBooks() throws Exception {
        // Arrange : Create some books so the list is not empty
        String bookJson1 = """
        {
            \"title\": \"Book 1\",
            \"author\": \"Author 1\",
            \"genre\": \"Genre 1\"
        }
        """;

        String bookJson2 = """
        {
            \"title\": \"Book 2\",
            \"author\": \"Author 2\",
            \"genre\": \"Genre 2\"
        }
        """;

        mockMvc.perform(post("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bookJson1))
            .andExpect(status().isOk());

            mockMvc.perform(post("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(bookJson2))
            .andExpect(status().isOk());

        // Act & Assert: Perform GET request and check response
        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].title").value("Book 1"))
            .andExpect(jsonPath("$[0].author").value("Author 1"))
            .andExpect(jsonPath("$[0].genre").value("Genre 1"))
            .andExpect(jsonPath("$[1].title").value("Book 2"))
            .andExpect(jsonPath("$[1].author").value("Author 2"))
            .andExpect(jsonPath("$[1].genre").value("Genre 2"))
            .andExpect(jsonPath("$[0].id").isNumber())
            .andExpect(jsonPath("$[1].id").isNumber());
    }

    @Test
    void getBookById_shouldReturnCorrectBook () throws Exception {
        // Arrange : Create some books so the list is not empty
        String bookJson = """
            {
              "title": "Book for ID Test",
              "author": "Author X",
              "genre": "Genre X"
            }
            """;

        // POST the book and capture the response - Extract the return ID
        MvcResult result = mockMvc.perform(post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                                .andExpect(status().isOk())
                                .andReturn();
        
        // Extract the ID from the response JSON
        String responseBody = result.getResponse().getContentAsString();
        Long id = objectMapper.readTree(responseBody).get("id").asLong();

        // Act & Assert: GET the book by ID and check the response
        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Book for ID Test"))
                .andExpect(jsonPath("$.author").value("Author X"))
                .andExpect(jsonPath("$.genre").value("Genre X"));
    }

    @Test
    void getBookById_whenBookDoesNotExist_shouldReturn404 () throws Exception {
        // Arrange an ID that doesn't exist
        Long nonExistentID = 0207L;

        mockMvc.perform(get("/api/books/{id}", nonExistentID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with ID: " + nonExistentID));
    }

    @Test
    void deleteBookById_shouldDeleteBook() throws Exception {
        // Arrange: Create a book and extract its ID
        String bookJson = """
            {
              "title": "Book for ID Test",
              "author": "Author X",
              "genre": "Genre X"
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bookJson))
                                .andExpect(status().isOk())
                                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Long id = objectMapper.readTree(responseBody).get("id").asLong();
        
       // Act & Assert: DELETE the book by ID and check the response
        mockMvc.perform(delete("/api/books/{id}", id))
                .andExpect(status().isNoContent());

        // Verify the book is actually deleted by trying to GET it
        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with ID: " + id));


        }

    @Test
    void getAverageRating_shouldReturnCorrectAverage () throws Exception {
        // Step 1: Create a book and extract its ID
        String bookJson = """
            {
                "title": "Book for Rating Test",
                "author": "Rating author",
                "genre": "Rating Genre"
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookJson))
                    .andExpect(status().isOk())
                    .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Long bookId = objectMapper.readTree(responseBody).get("id").asLong();

        // Step 2: Add reviews to the book
        String review1Json = """
            {
                "reviewer": "Putri",
                "comment": "Great book!",
                "rating": 5
            }
            """;

        String review2Json = """
            {
                "reviewer": "Khaireen",
                "comment": "Kind of a OK book",
                "rating": 3
            }
            """;

        // Add first review
        mockMvc.perform(post("/api/books/{bookId}/reviews", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(review1Json))
                .andExpect(status().isOk());

        // Add second review  
        mockMvc.perform(post("/api/books/{bookId}/reviews", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(review2Json))
                .andExpect(status().isOk());

        // Step 3: Get the average rating and verify it
        mockMvc.perform(get("/api/books/{bookId}/average-rating", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(4.0)); // Expected: (5 + 3) / 2 = 4.0

    }
}