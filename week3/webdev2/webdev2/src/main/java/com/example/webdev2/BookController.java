package com.example.webdev2;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final List<Book> books = new ArrayList<>();

    public BookController() {
        books.add(new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald"));
        books.add(new Book(2L, "1984", "George Orwell"));
        books.add(new Book(3L, "To Kill a Mockingbird", "Harper Lee"));
        books.add(new Book(4L, "The Hobbit", "J.R.R. Tolkien"));
    }

    @GetMapping
    @ResponseBody
    public List<Book> getBooks(
            @RequestParam(required = false) String author) {

        if (author == null || author.isBlank()) {
            return books;
        }

        return books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Book> getBook(@PathVariable Long id) {

        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createBook(@RequestBody Book book) {
        books.add(book);
        return "redirect:/books";
    }
}