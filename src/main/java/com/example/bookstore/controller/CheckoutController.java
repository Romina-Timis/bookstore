package com.example.bookstore.controller;

import com.example.bookstore.model.*;
import com.example.bookstore.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CheckoutController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarrelloRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

   @GetMapping("/checkout")
public String showCheckoutPage(Model model, Principal principal) {
    if (principal == null) {
        return "redirect:/login";
    }

    User user = userRepository.findByUsername(principal.getName());
    List<Carrello> cartItems = cartRepository.findByUserId(user.getId());

    if (cartItems == null || cartItems.isEmpty()) {
        return "redirect:/carrello";
    }

    // Crea mappa ID libro → oggetto Book
    Map<Long, Book> booksMap = bookRepository.findAll().stream()
            .collect(Collectors.toMap(Book::getId, b -> b));

    model.addAttribute("carrelli", cartItems);
    model.addAttribute("booksMap", booksMap);
    model.addAttribute("orderForm", new OrderForm());

    return "checkout";
}


    @PostMapping("/checkout")
    public String processOrder(@Valid @ModelAttribute OrderForm orderForm,
                               BindingResult bindingResult,
                               Principal principal,
                               Model model) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(principal.getName());
        List<Carrello> cartItems = cartRepository.findByUserId(user.getId());

        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/carrello";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("carrelli", cartItems);
            return "checkout";
        }

        Order order = new Order();
        order.setFullName(orderForm.getFullName());
        order.setAddress(orderForm.getAddress());
        order.setCity(orderForm.getCity());
        order.setPostalCode(orderForm.getPostalCode());
        order.setUser(user);

        for (Carrello cartItem : cartItems) {
            Book book = bookRepository.findById(cartItem.getBookId()).orElse(null);
            if (book == null) continue;

            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(book.getPrice());
            item.setOrder(order);
            order.getItems().add(item);
        }

        orderRepository.save(order);
        cartRepository.deleteAll(cartItems); // svuota il carrello dopo il checkout

        return "redirect:/confirmation?orderId=" + order.getId();
    }
}
