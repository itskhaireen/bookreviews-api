# Book Review API Documentation

## Overview
A comprehensive REST API for managing books and their reviews. Built with Spring Boot 3.5.0, JPA, and H2 database.

## Base URL
```
http://localhost:8080
```

## Available Endpoints

### Book Management

#### 1. Create a Book
**POST** `/api/books`

**Request Body:**
```json
{
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "genre": "Fiction"
}
```

**Response:**
```json
{
    "id": 1,
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "genre": "Fiction"
}
```

#### 2. Get All Books
**GET** `/api/books`

**Response:**
```json
[
    {
        "id": 1,
        "title": "The Great Gatsby",
        "author": "F. Scott Fitzgerald",
        "genre": "Fiction"
    }
]
```

#### 3. Get Book by ID
**GET** `/api/books/{id}`

**Response:**
```json
{
    "id": 1,
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "genre": "Fiction"
}
```

#### 4. Delete Book
**DELETE** `/api/books/{id}`

**Response:** `204 No Content`

#### 5. Get Average Rating
**GET** `/api/books/{id}/average-rating`

**Response:**
```json
4.5
```

### Review Management

#### 1. Add Review to Book
**POST** `/api/books/{bookId}/reviews`

**Request Body:**
```json
{
    "reviewer": "John Doe",
    "comment": "Excellent book! Highly recommended.",
    "rating": 5
}
```

**Response:**
```json
{
    "id": 1,
    "reviewer": "John Doe",
    "comment": "Excellent book! Highly recommended.",
    "rating": 5
}
```

#### 2. Get All Reviews for a Book
**GET** `/api/books/{bookId}/reviews`

**Response:**
```json
[
    {
        "id": 1,
        "reviewer": "John Doe",
        "comment": "Excellent book! Highly recommended.",
        "rating": 5
    },
    {
        "id": 2,
        "reviewer": "Jane Smith",
        "comment": "Good read, but could be better.",
        "rating": 4
    }
]
```

## Error Responses

### 400 Bad Request
```json
{
    "timestamp": "2025-06-29T13:00:26.608575",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed: Title cannot be empty",
    "path": "/api/books"
}
```

### 404 Not Found
```json
{
    "timestamp": "2025-06-29T13:00:26.608575",
    "status": 404,
    "error": "Not Found",
    "message": "Book not found with ID: 999",
    "path": "/api/books/999"
}
```

### 409 Conflict
```json
{
    "timestamp": "2025-06-29T13:00:26.608575",
    "status": 409,
    "error": "Conflict",
    "message": "Book already exists with title: The Great Gatsby and author: F. Scott Fitzgerald",
    "path": "/api/books"
}
```

### 503 Service Unavailable
```json
{
    "timestamp": "2025-06-29T13:00:26.608575",
    "status": 503,
    "error": "Service Unavailable",
    "message": "Database operation failed: save book",
    "path": "/api/books"
}
```

## Monitoring Endpoints

### Health Check
**GET** `/actuator/health`

**Response:**
```json
{
    "status": "UP",
    "components": {
        "db": {
            "status": "UP",
            "details": {
                "database": "H2",
                "validationQuery": "isValid()"
            }
        },
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 499963174912,
                "free": 499963174912,
                "threshold": 10485760
            }
        }
    }
}
```

### API Mappings
**GET** `/actuator/mappings`

Shows all available endpoints and their handlers.

### Application Info
**GET** `/actuator/info`

Shows application information.

## Database Console

### H2 Console
**URL:** `http://localhost:8080/h2-console`

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

## Testing the API

### Using curl

1. **Create a book:**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Test Book","author":"Test Author","genre":"Test Genre"}'
```

2. **Get all books:**
```bash
curl http://localhost:8080/api/books
```

3. **Add a review:**
```bash
curl -X POST http://localhost:8080/api/books/1/reviews \
  -H "Content-Type: application/json" \
  -d '{"reviewer":"John Doe","comment":"Great book!","rating":5}'
```

4. **Get average rating:**
```bash
curl http://localhost:8080/api/books/1/average-rating
```

## Validation Rules

### Book Validation
- `title`: Required, not empty
- `author`: Required, not empty
- `genre`: Required, not empty

### Review Validation
- `reviewer`: Required, not empty
- `comment`: Required, not empty
- `rating`: Required, between 1 and 5 (inclusive)

## Business Rules

1. **Book Uniqueness**: Books must have unique title-author combinations
2. **Review Association**: Reviews must be associated with existing books
3. **Average Rating**: Calculated as the mean of all ratings for a book
4. **Empty Reviews**: Books with no reviews return average rating of 0.0

## Technology Stack

- **Framework**: Spring Boot 3.5.0
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA
- **Validation**: Bean Validation (Jakarta)
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Documentation**: Spring Boot Actuator
- **Build Tool**: Maven

## Running the Application

1. **Start the application:**
```bash
./mvnw spring-boot:run
```

2. **Run tests:**
```bash
./mvnw test
```

3. **Access the API:**
   - Base URL: `http://localhost:8080`
   - Health Check: `http://localhost:8080/actuator/health`
   - H2 Console: `http://localhost:8080/h2-console`

## Integration Tests

The application includes comprehensive integration tests covering:
- ✅ All CRUD operations for books
- ✅ Review management
- ✅ Error scenarios (404, 400, 409, 503)
- ✅ Complex workflows (multiple reviews + average rating)
- ✅ Validation error handling
- ✅ Database transaction management

**Total Tests:** 66 (Unit + Integration)
- Unit Tests: 50
- Integration Tests: 15
- Application Tests: 1 