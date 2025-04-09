package com.example.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookstore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}