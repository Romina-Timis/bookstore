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

    @GetMapping
    public String viewCart(Principal principal, Model model) {
        User user = userRepo.findByUsername(principal.getName());
        List<Carrello> cartItems = cartRepo.findByUserId(user.getId());
        model.addAttribute("carrelli", cartItems);

        // Crea una mappa bookId -> Book
        Map<Long, Book> booksMap = bookRepo.findAll().stream()
                .collect(Collectors.toMap(Book::getId, book -> book));
        model.addAttribute("booksMap", booksMap);

        // Calcolo del totale
        BigDecimal totale = BigDecimal.ZERO;

        for (Carrello item : cartItems) {
            Book book = booksMap.get(item.getBookId());
            if (book != null && book.getPrice() != null) {
                BigDecimal subTot = book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                totale = totale.add(subTot);
            }
        }

        // Arrotondamento finale (fuori dal ciclo!)
        totale = totale.setScale(2, RoundingMode.HALF_UP);
        model.addAttribute("totale", totale);

        return "carrello";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userRepo.findByUsername(principal.getName());
        Carrello item = cartRepo.findById(id).orElse(null);

        if (item != null && item.getUserId().equals(user.getId())) {
            cartRepo.delete(item);
            redirectAttributes.addFlashAttribute("successMessage", "Libro rimosso dal carrello.");
        }

        return "redirect:/carrello";
    }

    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable Long id, @RequestParam int quantity, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        Carrello item = cartRepo.findById(id).orElse(null);

        if (item != null && item.getUserId().equals(user.getId()) && quantity > 0) {
            item.setQuantity(quantity);
            cartRepo.save(item);
        }

        return "redirect:/carrello";
    }

    @PostMapping("/clear")
    public String clearCart(Principal principal, RedirectAttributes redirectAttributes) {
        User user = userRepo.findByUsername(principal.getName());
        List<Carrello> items = cartRepo.findByUserId(user.getId());
        cartRepo.deleteAll(items);

        redirectAttributes.addFlashAttribute("successMessage", "Carrello svuotato con successo.");
        return "redirect:/carrello";
    }

}
