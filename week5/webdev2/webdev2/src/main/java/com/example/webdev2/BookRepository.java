package com.example.webdev2;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository {

    private final List<Book> books = new ArrayList<>();

    public BookRepository() {
        books.add(new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", 1925));
        books.add(new Book(2L, "1984", "George Orwell", 1949));
        books.add(new Book(3L, "To Kill a Mockingbird", "Harper Lee", 1960));
        books.add(new Book(4L, "The Hobbit", "J.R.R. Tolkien", 1937));
    }

    public List<Book> findAll() {
        return books;
    }

    public Book findById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Book save(Book book) {
        books.add(book);
        return book;
    }

    public Book update(Book updatedBook) {
        Book existingBook = findById(updatedBook.getId());

        if (existingBook != null) {
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPublicationYear(updatedBook.getPublicationYear());
        }

        return existingBook;
    }

    public boolean deleteById(Long id) {
        return books.removeIf(book -> book.getId().equals(id));
    }
}