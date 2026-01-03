package com.example.application.port.out;

import com.example.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {
    Customer save(Customer customer);
    Optional<Customer> findById(Long id);
    Optional<Customer> findByEmail(String email);
    List<Customer> findAll();
    void deleteById(Long id);
    boolean existsByEmail(String email);
}
