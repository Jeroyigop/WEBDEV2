# WEBDEV2 Lab 4 — Server-Rendered CRUD Interface

## Task 1 — Book List Template

Created `books.html` using Thymeleaf `th:each` to iterate through the list of books. The book title is displayed using `th:text`. An empty-state message is displayed using `th:if` when the list contains no books.

## Task 2 — Book Detail Template

Created `book-detail.html` for `GET /books/{id}`. The page displays the selected book's ID, title, and author using Thymeleaf variable expressions.

## Task 3 — Shared Navigation Fragment

Created `fragments/navbar.html` containing a reusable `navbar` fragment. The same fragment is included in both `books.html` and `book-detail.html` using `th:replace`.

## Task 4 — Create Form

Created `book-form.html` using `th:object` to bind the form to a `Book` object. The title and author fields use `th:field`, and the form submits to `POST /books`. After successful submission, the application redirects to `/books`.

## Task 5 — Binding Errors

Updated the `POST /books` controller method to use `@Valid @ModelAttribute Book book` immediately followed by `BindingResult result`. When validation errors occur, the form view is returned instead of saving the book. The `th:errors` expressions display field-specific validation messages for the title and author fields.
