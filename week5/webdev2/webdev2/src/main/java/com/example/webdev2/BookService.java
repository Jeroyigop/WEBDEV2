package com.example.webdev2;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        long newId = bookRepository.findAll().stream()
                .mapToLong(Book::getId)
                .max()
                .orElse(0) + 1;

        book.setId(newId);

        return bookRepository.save(book);
    }

    public Book updateBook(Book book) {
        return bookRepository.update(book);
    }

    public boolean deleteBook(Long id) {
        return bookRepository.deleteById(id);
    }
}