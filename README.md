# ShelfSpeak

*---because books finally speak™*

ShelfSpeak is a modern Spring Boot REST API for managing book reviews, emphasizing clean architecture, robust exception handling, and thorough testing with JUnit and Mockito. The project uses DTOs for clean API responses and includes both unit and integration tests to ensure reliability.

## Testing
- **Unit tests:** Comprehensive coverage for service and repository layers
- **Integration tests:**
  - `AuthControllerIntegrationTest` (**complete & passing**)
  - `BookControllerIntegrationTest` (**complete & passing**)
  - `ReviewControllerIntegrationTest` (**complete & passing**)

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
- The Postman collection (`BookReviewAPI.postman_collection.json`) is **up-to-date** with authentication and payload requirements for all endpoints.

## Authentication Note
- For all protected endpoints (book creation, review creation, etc.), you must include a valid JWT token in the `Authorization` header: `Bearer <token>`

## Recent Updates
See [CHANGELOG.md](CHANGELOG.md) for recent fixes and updates.

## Admin Account & Role Management

- By default, **no admin user exists** in development or production. All users registered via the API are assigned the `USER` role.
- **Admin-only features** (such as deleting books or changing user roles) require at least one user with the `ADMIN` role.
- To test or use admin features, you must manually create an admin user in your database. For example, in MySQL:

  ```sql
  -- Generate a BCrypt hash for your chosen password (do not use plain text)
  INSERT INTO users (username, email, password, role)
  VALUES ('admin', 'admin@example.com', '<bcrypt_hash>', 'ADMIN');
  ```
- After creating the admin, log in via `/auth/login` to obtain a JWT token for admin actions.
- **Security Note:** Always use a strong, unique password for the admin account and change it after first login.

### Testing
- Automated tests are unaffected: they programmatically create admin users as needed and fully cover admin scenarios.