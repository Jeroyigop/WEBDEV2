package com.example.webdev2;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String getBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable Long id, Model model) {

        Book book = bookService.getBookById(id);

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

        bookService.createBook(book);

        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        Book book = bookService.getBookById(id);

        if (book == null) {
            return "redirect:/books";
        }

        model.addAttribute("book", book);

        return "book-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBook(
            @PathVariable Long id,
            @Valid @ModelAttribute Book book,
            BindingResult result) {

        if (result.hasErrors()) {
            return "book-edit";
        }

        book.setId(id);
        bookService.updateBook(book);

        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        return "redirect:/books";
    }
}