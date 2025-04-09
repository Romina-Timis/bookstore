package com.example.bookstore.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// configurazione di sicurezza, gestisce la sicurezza del sito
@Configuration // configurazione di Spring
@EnableWebSecurity // attiva la sicurezza web di Spring
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final LoginHandler loginHandler;
    // costruttore 
    public SecurityConfig(CustomUserDetailsService userDetailsService, LoginHandler loginHandler) {
        this.userDetailsService = userDetailsService; // recupera info dell'utente
        this.loginHandler = loginHandler; // indirizza gli utenti in base al loro ruolo
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider()) // provider per autenticazione, verifica credenziali
            .authorizeHttpRequests(auth -> auth  // definisce le regole
                .requestMatchers("/", "/login", "/register", "/css/**").permitAll() // aggiungi "/register" per permettere l'accesso alla registrazione
                .requestMatchers("/admin/**").hasRole("ADMIN") // solo admin può accedere a queste pagine
                .requestMatchers("/books/**", "/carrello/**").hasRole("USER") // solo gli utenti possono accedere a queste pagine
                .anyRequest().authenticated() // qualsiasi altra richiesta deve essere autenticata
            )
            .formLogin(form -> form // comportamento del login
                .loginPage("/login") // pagina di login personalizzata
                .successHandler(loginHandler)  // reindirizza l'utente in base al suo ruolo
                .permitAll() // chiunque può accedere alla pagina di login
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }

    // metodo per autenticare gli utenti
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // metodo per codificare la password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // NoOp per semplicità
    }
}
