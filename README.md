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

- In the **development environment**, an admin user is automatically created at startup if none exists:
  - Username: `admin`
  - Password: `admin123`
  - Email: `admin@bookreview.com`
  - **Change these credentials immediately if using for anything beyond local development!**
- In **production**, no admin user is created by default. You must manually create an admin user in your database. For example, in MySQL:
  
  ```sql
  -- Generate a BCrypt hash for your chosen password (do not use plain text)
  INSERT INTO users (username, email, password, role)
  VALUES ('admin', 'admin@example.com', '<bcrypt_hash>', 'ADMIN');
  ```
- After creating the admin, log in via `/auth/login` to obtain a JWT token for admin actions.
- **Security Note:** Always use a strong, unique password for the admin account and change it after first login.
- **Role-based access is fully supported in the dev environment**—you can test both USER and ADMIN features out of the box.

## JWT Secret Configuration

- For learning/demo purposes, a sample `jwt.secret` is included in `application-prod.properties`:
  ```properties
  # WARNING: This is a demo secret for learning purposes only!
  jwt.secret=DemoProdSecretKey1234567890
  jwt.expirationMs=86400000
  ```
- **In real production, never commit secrets to source control.** Use environment variables or a secure secrets manager instead.

### Production Deployment with Environment Variables

For production deployments, use environment variables to set sensitive configuration:

```bash
# Set environment variables
export JWT_SECRET=your_super_secure_production_secret_here
export JWT_EXPIRATION_MS=86400000
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:mysql://your-db-host:3306/book_review_db
export SPRING_DATASOURCE_USERNAME=your_db_username
export SPRING_DATASOURCE_PASSWORD=your_db_password
```

Then update `application-prod.properties` to use environment variables:
```properties
jwt.secret=${JWT_SECRET}
jwt.expirationMs=${JWT_EXPIRATION_MS:86400000}
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

### Testing
- Automated tests are unaffected: they programmatically create admin users as needed and fully cover admin scenarios.