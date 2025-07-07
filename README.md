# ShelfSpeak

*---because books finally speak™*

ShelfSpeak is a modern Spring Boot REST API for managing book reviews, emphasizing clean architecture, robust exception handling, and thorough testing with JUnit and Mockito. The project uses DTOs for clean API responses and includes both unit and integration tests to ensure reliability.

## Testing
- **Unit tests:** Comprehensive coverage for service and repository layers
- **Integration tests:**
  - `AuthControllerIntegrationTest` (**complete & passing**)
  - `BookControllerIntegrationTest` (**complete & passing**)
  - `ReviewControllerIntegrationTest` (under development)

## Debugging & Test Output
- All test output is available in `target/surefire-reports/` after running tests.
- To view logs and stack traces for a test class:
  ```sh
  cat target/surefire-reports/com.example.bookreviewapi.controller.BookControllerIntegrationTest.txt | tail -40
  ```
- 503 errors are mapped from database/internal errors; see logs for details.

## Features
- User registration, JWT authentication, and role-based access control
- CRUD operations for books and reviews
- Robust exception handling with proper HTTP status codes
- DTOs for clean and maintainable API responses
- Environment-specific profiles for development and production
- **Sentiment analysis for reviews using external APIs (e.g., HuggingFace, Google NLP) is under development**

## Postman Collection
- The Postman collection (`BookReviewAPI.postman_collection.json`) is **under development** and may not reflect all current endpoints or request/response formats.

## Recent Updates
See [CHANGELOG.md](CHANGELOG.md) for recent fixes and updates.