package com.example.webdev2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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
    public String getBooks(
            @RequestParam(required = false) String author,
            Model model) {

        List<Book> filteredBooks;

        if (author == null || author.isBlank()) {
            filteredBooks = books;
        } else {
            filteredBooks = books.stream()
                    .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                    .toList();
        }

        model.addAttribute("books", filteredBooks);

        return "books";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable Long id, Model model) {

        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (book == null) {
            return "redirect:/books";
        }

        model.addAttribute("book", book);

        return "book-detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("book", new Book());

        return "book-form";
    }

    @PostMapping
public String createBook(
        @Valid @ModelAttribute Book book,
        BindingResult result) {

    if (result.hasErrors()) {
        return "book-form";
    }

    long newId = books.stream()
            .mapToLong(Book::getId)
            .max()
            .orElse(0) + 1;

    book.setId(newId);

    books.add(book);

    return "redirect:/books";
}
}