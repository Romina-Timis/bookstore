package com.example.bookstore.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.example.bookstore.model.User;
import com.example.bookstore.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {  // fondamentale per recuperare un utente dal db e restituire le credenziali 

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) { 
        this.userRepository = userRepository;
    }
    // metodo principale della classe per recuperare i dettagli dell'utente 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) { // se l'utente non viene trovato viene lanciata un'eccezione
            throw new UsernameNotFoundException("Utente non trovato con username: " + username);
        }
    
        return new CustomUserDetails(user);
    }
}
