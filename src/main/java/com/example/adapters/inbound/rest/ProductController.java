package com.example.adapters.inbound.rest;

import com.example.adapters.inbound.rest.dto.ProductRequest;
import com.example.adapters.inbound.rest.dto.ProductResponse;
import com.example.application.port.in.CreateProductUseCase;
import com.example.application.port.in.GetProductUseCase;
import com.example.application.port.in.command.CreateProductCommand;
import com.example.domain.exception.ProductNotFoundException;
import com.example.domain.model.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;

    public ProductController(CreateProductUseCase createProductUseCase, GetProductUseCase getProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getProductUseCase = getProductUseCase;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        CreateProductCommand command = new CreateProductCommand(
                request.sku(),
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity()
        );
        Product product = createProductUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = getProductUseCase.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ResponseEntity.ok(toResponse(product));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> getProductBySku(@PathVariable String sku) {
        Product product = getProductUseCase.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));
        return ResponseEntity.ok(toResponse(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = getProductUseCase.findAll();
        List<ProductResponse> responses = products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
