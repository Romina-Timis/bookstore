package com.example.bookstore.controller;

import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookRepository bookRepository;

    // Mostra la lista dei libri
    @GetMapping
    public String showBookList(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "admin-books";
    }

    // Aggiungi nuovo libro
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    // Salva nuovo libro
    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book, RedirectAttributes redirectAttributes) {
        bookRepository.save(book);
        redirectAttributes.addFlashAttribute("successMessage", "Libro aggiunto con successo!");
        return "redirect:/admin/books";
    }

    // Modifica libro esistente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID non valido: " + id));
        model.addAttribute("book", book);
        return "edit-book";
    }

    // Salva modifiche libro
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute("book") Book updatedBook,
                             RedirectAttributes redirectAttributes) {
        Book existing = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Libro non trovato"));

        existing.setTitle(updatedBook.getTitle());
        existing.setAuthor(updatedBook.getAuthor());
        existing.setDescription(updatedBook.getDescription());
        existing.setPrice(updatedBook.getPrice());
        existing.setImageUrl(updatedBook.getImageUrl());

        bookRepository.save(existing);
        redirectAttributes.addFlashAttribute("successMessage", "Libro aggiornato correttamente!");
        return "redirect:/admin/books";
    }

    // Elimina libro
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/admin/books";
    }
}
