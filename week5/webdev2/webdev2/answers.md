# WEBDEV2 Lab 5 — Custom Domain Simple CRUD Application

## Task 1 — Domain Selection & Requirements

The selected domain is a Book Library Management System. The application manages books and supports creating, viewing, editing, and deleting book records. The functional requirements are documented in `crud-app-spec.md`.

## Task 2 — Validated Domain Model

The `Book` entity contains four fields: `id`, `title`, `author`, and `publicationYear`. Jakarta Bean Validation is used with `@NotBlank`, `@Size`, `@Min`, and `@Max` constraints. Each constraint includes a custom validation error message.

## Task 3 — Service & Repository Layers

The application follows the Controller-Service-Repository architecture. `BookRepository` manages the in-memory list of books and provides methods for saving, finding, updating, retrieving, and deleting books. `BookService` contains the business operations and delegates persistence tasks to `BookRepository`.

## Task 4 — Web Controller & Thymeleaf CRUD Views

`BookController` handles the web routes for listing, viewing, creating, editing, and deleting books. Thymeleaf templates provide the user interface for all CRUD operations. The create and edit forms use `th:object` and `th:field` for model binding and display validation errors using `th:errors`.

## CRUD Operations

* Create: `/books/new` and `POST /books`
* Read: `GET /books` and `GET /books/{id}`
* Update: `/books/edit/{id}`
* Delete: `/books/delete/{id}`

## Validation

Invalid or incomplete form submissions are returned to the corresponding form instead of being saved. Field-specific validation messages are displayed beside the affected inputs.
