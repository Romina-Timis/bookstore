package com.example.bookstore.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookstore.model.Carrello;

import java.util.List;

public interface CarrelloRepository extends JpaRepository<Carrello, Long> {
    List<Carrello> findByUserId(Long userId); // trova gli articoli nel carrello per userId
    Carrello findByUserIdAndBookId(Long userId, Long bookId); // trova un articolo nel carrello per userId e bookId
}
