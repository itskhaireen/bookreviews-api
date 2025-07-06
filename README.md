# ShelfSpeak

*---because books finally speak™*

ShelfSpeak is a modern Spring Boot REST API for managing book reviews, emphasizing clean architecture, robust exception handling, and thorough testing with JUnit and Mockito. The project uses DTOs for clean API responses and includes both unit and integration tests to ensure reliability.

## Testing
- **Unit tests:** Comprehensive coverage for service and repository layers
- **Integration tests:**
  - `AuthControllerIntegrationTest` (complete)
  - `BookControllerIntegrationTest` (under development)
  - `ReviewControllerIntegrationTest` (under development)

## Features
- User registration, JWT authentication, and role-based access control
- CRUD operations for books and reviews
- Robust exception handling with proper HTTP status codes
- DTOs for clean and maintainable API responses
- Environment-specific profiles for development and production
- **Sentiment analysis for reviews using external APIs (e.g., HuggingFace, Google NLP) is under development**
