package com.example.bookstore.security;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws IOException, ServletException {

        // Reindirizza in base al ruolo
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(auth.getAuthority())) {
                response.sendRedirect("/admin/books");
                return;
            } else if ("ROLE_USER".equals(auth.getAuthority())) {
                response.sendRedirect("/books");
                return;
            }
        }

        // Ruolo sconosciuto, fallback su index
        response.sendRedirect("/");
    }
}
