package com.example.bookstore.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConfermaOrdineController {

    @GetMapping("/confirmation")
    public String confermaOrdine(@RequestParam(name = "orderId", required = false) Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "confirmation"; // questo corrisponde a confirmation.html in /templates
    }
}
