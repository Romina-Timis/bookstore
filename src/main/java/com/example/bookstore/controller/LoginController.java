package com.example.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    // metodo per il modulo di login
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }
    // metodo per il logout
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("successMessage", "Logout effettuato con successo!");
        return "redirect:/login"; 
    }

}
