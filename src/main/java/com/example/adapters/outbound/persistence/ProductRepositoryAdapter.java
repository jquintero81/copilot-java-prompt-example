package com.example.adapters.outbound.persistence;

import com.example.application.port.out.ProductRepositoryPort;
import com.example.domain.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final SpringDataProductRepository springDataProductRepository;

    public ProductRepositoryAdapter(SpringDataProductRepository springDataProductRepository) {
        this.springDataProductRepository = springDataProductRepository;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity saved = springDataProductRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return springDataProductRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return springDataProductRepository.findBySku(sku).map(this::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return springDataProductRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        springDataProductRepository.deleteById(id);
    }

    @Override
    public boolean existsBySku(String sku) {
        return springDataProductRepository.existsBySku(sku);
    }

    private ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setSku(product.getSku());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStockQuantity(product.getStockQuantity());
        entity.setCreatedAt(product.getCreatedAt());
        entity.setUpdatedAt(product.getUpdatedAt());
        return entity;
    }

    private Product toDomain(ProductEntity entity) {
        Product product = new Product(
                entity.getId(),
                entity.getSku(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStockQuantity()
        );
        product.setCreatedAt(entity.getCreatedAt());
        product.setUpdatedAt(entity.getUpdatedAt());
        return product;
    }
}
