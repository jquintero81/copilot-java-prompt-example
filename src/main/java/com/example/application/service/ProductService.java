package com.example.application.service;

import com.example.application.port.in.command.CreateProductCommand;
import com.example.application.port.in.CreateProductUseCase;
import com.example.application.port.in.GetProductUseCase;
import com.example.application.port.out.ProductRepositoryPort;
import com.example.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements CreateProductUseCase, GetProductUseCase {

    private final ProductRepositoryPort productRepository;

    public ProductService(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product execute(CreateProductCommand command) {
        validateProductCommand(command);
        
        if (productRepository.existsBySku(command.sku())) {
            throw new IllegalArgumentException("Product with SKU " + command.sku() + " already exists");
        }

        Product product = new Product(
                null,
                command.sku(),
                command.name(),
                command.description(),
                command.price(),
                command.stockQuantity()
        );

        return productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    private void validateProductCommand(CreateProductCommand command) {
        if (command.sku() == null || command.sku().isBlank()) {
            throw new IllegalArgumentException("SKU cannot be empty");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (command.price() == null || command.price().signum() <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (command.stockQuantity() == null || command.stockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
    }
}
