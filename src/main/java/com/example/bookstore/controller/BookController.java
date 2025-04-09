package com.example.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Carrello;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CarrelloRepository;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.BookService;

import java.security.Principal;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private CarrelloRepository cartRepo;
    

    @GetMapping("/books")
    public String viewBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books"; // Assicurati che il file template si chiami books.html in
                        // src/main/resources/templates
    }

    @PostMapping("/carrello/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, Principal principal, RedirectAttributes redirectAttributes) {
        // Ottieni l'utente corrente
        User user = userRepo.findByUsername(principal.getName());
        
        // Verifica se il libro è già nel carrello dell'utente
        Carrello existing = cartRepo.findByUserIdAndBookId(user.getId(), bookId);
        
        if (existing != null) {
            // Se il libro è già nel carrello, aggiorna la quantità
            existing.setQuantity(existing.getQuantity() + 1);
            cartRepo.save(existing);
        } else {
            // Se il libro non è nel carrello, crealo
            Carrello newItem = new Carrello();
            newItem.setBookId(bookId);
            newItem.setUserId(user.getId());
            newItem.setQuantity(1); // Aggiungi una copia del libro
            cartRepo.save(newItem);
        }
    
        // Aggiungi un messaggio di successo e l'URL per il carrello
        redirectAttributes.addFlashAttribute("successMessage", "Libro aggiunto al carrello con successo!");
        redirectAttributes.addFlashAttribute("goToCart", "/carrello");
        return "redirect:/books"; // Torna alla pagina dei libri
    }
    
    


}
