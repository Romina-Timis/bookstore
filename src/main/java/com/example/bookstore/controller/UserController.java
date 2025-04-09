package com.example.bookstore.controller;



import com.example.bookstore.model.User;
import com.example.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Mostra il form di registrazione
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Assicurati che esista register.html in templates
    }

    // Salva l'utente registrato
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Imposta il ruolo di default
        user.setRole("ROLE_USER");

        // Salva l'utente nel database
        userRepository.save(user);

        // Mostra un messaggio di successo e reindirizza alla pagina di login
        model.addAttribute("successMessage", "Registrazione completata! Ora puoi accedere.");
        return "login";
    }
}
