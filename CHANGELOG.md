# Changelog

All notable changes to ShelfSpeak will be documented in this file.

## [Unreleased]
- Postman collection (`BookReviewAPI.postman_collection.json`) is under development and may not reflect all current endpoints or request/response formats.

## [2025-07-07]
### Fixed
- Fixed a bug where review creation failed due to missing timestamps (`createdAt`, `updatedAt`).
- Fixed a bug where reviews could be created with a null or empty username, causing validation errors and inconsistent data. Now, reviews require a non-empty username for the associated user.

### Added
- Integration tests now fully cover authentication and book endpoints, including security and error handling.
- Updated documentation to clarify endpoint security and test output access.

## [2025-06-25]
### Fixed
- Fixed a **LAZY LOADING** issue: Switched the `reviews` relationship in the `Book` entity to `FetchType.EAGER` to ensure reviews are loaded with books, preventing `LazyInitializationException` during serialization and in controller responses. 