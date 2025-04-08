package com.example.bookstore.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.bookstore.model.Carrello;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CarrelloRepository;
import com.example.bookstore.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CarrelloController {

    @Autowired
    private CarrelloRepository cartRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());

        Carrello existing = cartRepo.findByUserIdAndBookId(user.getId(), bookId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + 1);
        } else {
            Carrello newItem = new Carrello();
            newItem.setBookId(bookId);
            newItem.setUserId(user.getId());
            newItem.setQuantity(1);
            cartRepo.save(newItem);
        }

        return "redirect:/books";
    }

    @GetMapping
    public String viewCart(Principal principal, Model model) {
        User user = userRepo.findByUsername(principal.getName());
        List<Carrello> cartItems = cartRepo.findByUserId(user.getId());
        model.addAttribute("cartItems", cartItems);

        // Passiamo anche i libri per i dettagli
        model.addAttribute("booksMap", bookRepo.findAll());
        return "cart";
    }
}
