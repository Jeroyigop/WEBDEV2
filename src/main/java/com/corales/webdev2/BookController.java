package com.corales.webdev2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();

    public BookController() {
        books.add(new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald"));
        books.add(new Book(2L, "1984", "George Orwell"));
        books.add(new Book(3L, "To Kill a Mockingbird", "Harper Lee"));
    }

    @GetMapping
    @ResponseBody
    public List<Book> getBooks(
            @RequestParam(required = false) String author) {

        if (author == null || author.isBlank()) {
            return books;
        }

        List<Book> filteredBooks = new ArrayList<>();

        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                filteredBooks.add(book);
            }
        }

        return filteredBooks;
    }

    @GetMapping("/{id}")
@ResponseBody
public ResponseEntity<Book> getBook(@PathVariable Long id) {

    for (Book book : books) {
        if (book.getId().equals(id)) {
            return ResponseEntity.ok(book);
        }
    }

    return ResponseEntity.notFound().build();
}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createBook(
            @RequestParam Long id,
            @RequestParam String title,
            @RequestParam String author) {

        books.add(new Book(id, title, author));

        return "redirect:/books";
    }
}