package com.example.bookstore.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.bookstore.model.User;

public class CustomUserDetails implements UserDetails {  // fondamentale per l'autenticazione e autorizzazione utenti e per memorizzare i dettagli dell'utente

    private final User user;
    
    public CustomUserDetails(User user) {
        this.user = user;
    }
    // restituisce i ruoli/permessi
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Il ruolo nel DB deve essere ad esempio "ROLE_USER" o "ROLE_ADMIN"
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }
    // restituisce la password dell'utente
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    // restituisce l'username dell'utente
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    // restituisce true ovvero che l'account non è scaduto
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    // restituisce true ovvero che l'account non è bloccato
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    // restituisce ture ovvero che le credenziali non sono scadute
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    // account abilitato
    @Override
    public boolean isEnabled() {
        return true;
    }
}
