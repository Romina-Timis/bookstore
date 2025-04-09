package com.example.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Carrello;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CarrelloRepository;
import com.example.bookstore.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloRepository cartRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private UserRepository userRepo;

    // metodo per visualizzare il carrello dell'utente
    @GetMapping
    public String viewCart(Principal principal, Model model) {
        User user = userRepo.findByUsername(principal.getName());
        List<Carrello> cartItems = cartRepo.findByUserId(user.getId()); // recupera i prodotti nel carrello
        model.addAttribute("carrelli", cartItems);

        // crea una mappa per associale l'id del libro all'oggetto Book
        Map<Long, Book> booksMap = bookRepo.findAll().stream()
                .collect(Collectors.toMap(Book::getId, book -> book));
        model.addAttribute("booksMap", booksMap);

        // calcola il totale del carrello
        BigDecimal totale = BigDecimal.ZERO;

        for (Carrello item : cartItems) {   // itera gli articoli e somma il totale
            Book book = booksMap.get(item.getBookId());
            if (book != null && book.getPrice() != null) {
                BigDecimal subTot = book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                totale = totale.add(subTot);
            }
        }

        // arrotonda a due decimali
        totale = totale.setScale(2, RoundingMode.HALF_UP);
        model.addAttribute("totale", totale);

        return "carrello";
    }
    // metodo per rimuovere un libro dal carrello
    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userRepo.findByUsername(principal.getName());
        Carrello item = cartRepo.findById(id).orElse(null); // trova il prodotto nel carrello usando l'id
        // verifica se il prodotto esiste e se appartiene all'utente
        if (item != null && item.getUserId().equals(user.getId())) {
            cartRepo.delete(item); // elimina il prodotto
            redirectAttributes.addFlashAttribute("successMessage", "Libro rimosso dal carrello.");
        }
        return "redirect:/carrello";
    }
    // metodo per aggiornare la quantità di un prodotto nel carrello
    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable Long id, @RequestParam int quantity, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        Carrello item = cartRepo.findById(id).orElse(null); // trova l'articolo usando l'id
        // verifica se l'articolo esiste, se appartiene all'utente e se la quantità è maggiore di 0
        if (item != null && item.getUserId().equals(user.getId()) && quantity > 0) {
            item.setQuantity(quantity); // imposta la nuova quantità 
            cartRepo.save(item); // salva le modifiche
        }

        return "redirect:/carrello";
    }
    
    // metodo per svuotare il carrello
    @PostMapping("/clear")
    public String clearCart(Principal principal, RedirectAttributes redirectAttributes) {
        User user = userRepo.findByUsername(principal.getName());
        List<Carrello> items = cartRepo.findByUserId(user.getId());
        cartRepo.deleteAll(items); // elimina tutti gli articoli del carrello 

        redirectAttributes.addFlashAttribute("successMessage", "Carrello svuotato con successo.");
        return "redirect:/carrello";
    }

}
