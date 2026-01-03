package com.example.application.port.out;

import com.example.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findAll();
    void deleteById(Long id);
}
