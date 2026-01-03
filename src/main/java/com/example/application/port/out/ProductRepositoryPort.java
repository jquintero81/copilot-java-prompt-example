package com.example.application.port.out;

import com.example.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Optional<Product> findBySku(String sku);
    List<Product> findAll();
    void deleteById(Long id);
    boolean existsBySku(String sku);
}
