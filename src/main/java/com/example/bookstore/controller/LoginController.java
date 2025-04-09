package com.example.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // restituisce il tamplate "login.html"
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        // Aggiungi il messaggio di successo del logout
        redirectAttributes.addFlashAttribute("successMessage", "Logout effettuato con successo!");
        return "redirect:/login"; // Reindirizza alla pagina di login
    }

}
