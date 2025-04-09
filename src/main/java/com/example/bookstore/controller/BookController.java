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
    private BookService bookService; // gestione dei libri

    @Autowired
    private UserRepository userRepo; // repository per gli utenti
    
    @Autowired
    private CarrelloRepository cartRepo; // repository per il carrello
    
    // metodo get per visualizzare i libri nella pagina books
    @GetMapping("/books")
    public String viewBooks(Model model) {
        List<Book> books = bookService.getAllBooks(); // recupera la lista di tutti i libri 
        model.addAttribute("books", books); // aggiunge la lista di libri al modello
        return "books"; 
    }
    // metodo per aggiungere un libro al carrello
    @PostMapping("/carrello/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userRepo.findByUsername(principal.getName());// recupera l'utente autentificato
        Carrello existing = cartRepo.findByUserIdAndBookId(user.getId(), bookId); // verifica se il libro è già nel carrello dell'utente
        
        if (existing != null) {    // se il libro è già nel carrello, aggiorna la quantità e la incrementa di 1
            existing.setQuantity(existing.getQuantity() + 1);
            cartRepo.save(existing);
        } else { // altrimenti se il libro non è nel carrello, crea un nuovo oggetto Carrello
            Carrello newItem = new Carrello();
            newItem.setBookId(bookId);
            newItem.setUserId(user.getId());
            newItem.setQuantity(1); // aggiunge una copia del libro
            cartRepo.save(newItem);
        }
    
        // messaggio di successo
        redirectAttributes.addFlashAttribute("successMessage", "Libro aggiunto al carrello con successo!");
        redirectAttributes.addFlashAttribute("goToCart", "/carrello");
        return "redirect:/books"; 
    }
    
    


}
