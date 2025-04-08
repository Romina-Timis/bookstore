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
@RequestMapping("/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloRepository cartRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/add/{bookId}")
public String addToCart(@PathVariable Long bookId, Principal principal) {
    User user = userRepo.findByUsername(principal.getName());

    Carrello existing = cartRepo.findByUserIdAndBookId(user.getId(), bookId);
    if (existing != null) {
        existing.setQuantity(existing.getQuantity() + 1);
        cartRepo.save(existing);
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

    model.addAttribute("carrelli", cartItems);
    model.addAttribute("books", bookRepo.findAll());

    // Calcola il totale
    double totale = cartItems.stream()
        .mapToDouble(item -> {
            return bookRepo.findById(item.getBookId())
                    .map(book -> book.getPrice().doubleValue() * item.getQuantity())
                    .orElse(0.0);
        })
        .sum();

    model.addAttribute("totale", totale);

    return "carrello";
}

    
    @GetMapping("/remove/{id}")
public String removeFromCart(@PathVariable Long id, Principal principal) {
    User user = userRepo.findByUsername(principal.getName());
    Carrello item = cartRepo.findById(id).orElse(null);

    if (item != null && item.getUserId().equals(user.getId())) {
        cartRepo.delete(item);
    }

    return "redirect:/carrello";
}




}
