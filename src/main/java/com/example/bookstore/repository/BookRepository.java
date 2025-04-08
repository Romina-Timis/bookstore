package com.example.bookstore.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Qui puoi aggiungere metodi personalizzati se ti servono (es: findByTitle)
}
