package com.example.bookstore.security;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // componente gestita da Spring
public class LoginHandler implements AuthenticationSuccessHandler { // fondamentale per gestire il login non andato a buon fine
// in base al ruolo dell'utente la classe reindirizza l'utente verso una pagina specifica

    // metodo chiamato automaticamente da spring dopo login con successo
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication)
                                        throws IOException, ServletException {
        for (GrantedAuthority auth : authentication.getAuthorities()) { 
            if ("ROLE_ADMIN".equals(auth.getAuthority())) {
                response.sendRedirect("/admin/books");
                return;
            } else if ("ROLE_USER".equals(auth.getAuthority())) {
                response.sendRedirect("/books");
                return;
            }
        }

        response.sendRedirect("/");
    }
}
