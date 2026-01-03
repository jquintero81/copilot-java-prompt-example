package com.example.application.port.in;

import com.example.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface GetProductUseCase {
    Optional<Product> findById(Long id);
    Optional<Product> findBySku(String sku);
    List<Product> findAll();
}
