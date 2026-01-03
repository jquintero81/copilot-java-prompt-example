package com.example.application.service;

import com.example.application.port.in.command.CreateProductCommand;
import com.example.application.port.out.ProductRepositoryPort;
import com.example.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private ProductService productService;

    private CreateProductCommand validCommand;
    private Product validProduct;

    @BeforeEach
    void setUp() {
        validCommand = new CreateProductCommand(
                "SKU-001",
                "Test Product",
                "Test Description",
                new BigDecimal("19.99"),
                100
        );
        
        validProduct = new Product(
                1L,
                "SKU-001",
                "Test Product",
                "Test Description",
                new BigDecimal("19.99"),
                100
        );
    }

    @Test
    @DisplayName("givenValidCommand_whenCreateProduct_thenProductIsSaved")
    void givenValidCommand_whenCreateProduct_thenProductIsSaved() {
        // Given
        given(productRepository.existsBySku(validCommand.sku())).willReturn(false);
        given(productRepository.save(any(Product.class))).willReturn(validProduct);

        // When
        Product result = productService.execute(validCommand);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("givenValidCommand_whenCreateProduct_thenProductHasCorrectSku")
    void givenValidCommand_whenCreateProduct_thenProductHasCorrectSku() {
        // Given
        given(productRepository.existsBySku(validCommand.sku())).willReturn(false);
        given(productRepository.save(any(Product.class))).willReturn(validProduct);

        // When
        Product result = productService.execute(validCommand);

        // Then
        assertThat(result.getSku()).isEqualTo("SKU-001");
    }

    @Test
    @DisplayName("givenDuplicateSku_whenCreateProduct_thenThrowsException")
    void givenDuplicateSku_whenCreateProduct_thenThrowsException() {
        // Given
        given(productRepository.existsBySku(validCommand.sku())).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> productService.execute(validCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("givenDuplicateSku_whenCreateProduct_thenProductIsNotSaved")
    void givenDuplicateSku_whenCreateProduct_thenProductIsNotSaved() {
        // Given
        given(productRepository.existsBySku(validCommand.sku())).willReturn(true);

        // When & Then
        try {
            productService.execute(validCommand);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        then(productRepository).should(never()).save(any(Product.class));
    }

    @Test
    @DisplayName("givenNullSku_whenCreateProduct_thenThrowsException")
    void givenNullSku_whenCreateProduct_thenThrowsException() {
        // Given
        CreateProductCommand invalidCommand = new CreateProductCommand(
                null,
                "Test Product",
                "Test Description",
                new BigDecimal("19.99"),
                100
        );

        // When & Then
        assertThatThrownBy(() -> productService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("SKU cannot be empty");
    }

    @Test
    @DisplayName("givenBlankSku_whenCreateProduct_thenThrowsException")
    void givenBlankSku_whenCreateProduct_thenThrowsException() {
        // Given
        CreateProductCommand invalidCommand = new CreateProductCommand(
                "   ",
                "Test Product",
                "Test Description",
                new BigDecimal("19.99"),
                100
        );

        // When & Then
        assertThatThrownBy(() -> productService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("SKU cannot be empty");
    }

    @Test
    @DisplayName("givenNullName_whenCreateProduct_thenThrowsException")
    void givenNullName_whenCreateProduct_thenThrowsException() {
        // Given
        CreateProductCommand invalidCommand = new CreateProductCommand(
                "SKU-001",
                null,
                "Test Description",
                new BigDecimal("19.99"),
                100
        );

        // When & Then
        assertThatThrownBy(() -> productService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name cannot be empty");
    }

    @Test
    @DisplayName("givenNegativePrice_whenCreateProduct_thenThrowsException")
    void givenNegativePrice_whenCreateProduct_thenThrowsException() {
        // Given
        CreateProductCommand invalidCommand = new CreateProductCommand(
                "SKU-001",
                "Test Product",
                "Test Description",
                new BigDecimal("-10.00"),
                100
        );

        // When & Then
        assertThatThrownBy(() -> productService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Price must be positive");
    }

    @Test
    @DisplayName("givenNegativeStock_whenCreateProduct_thenThrowsException")
    void givenNegativeStock_whenCreateProduct_thenThrowsException() {
        // Given
        CreateProductCommand invalidCommand = new CreateProductCommand(
                "SKU-001",
                "Test Product",
                "Test Description",
                new BigDecimal("19.99"),
                -10
        );

        // When & Then
        assertThatThrownBy(() -> productService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Stock quantity cannot be negative");
    }

    @Test
    @DisplayName("givenExistingProductId_whenFindById_thenReturnsProduct")
    void givenExistingProductId_whenFindById_thenReturnsProduct() {
        // Given
        given(productRepository.findById(1L)).willReturn(Optional.of(validProduct));

        // When
        Optional<Product> result = productService.findById(1L);

        // Then
        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("givenNonExistingProductId_whenFindById_thenReturnsEmpty")
    void givenNonExistingProductId_whenFindById_thenReturnsEmpty() {
        // Given
        given(productRepository.findById(999L)).willReturn(Optional.empty());

        // When
        Optional<Product> result = productService.findById(999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("givenExistingSku_whenFindBySku_thenReturnsProduct")
    void givenExistingSku_whenFindBySku_thenReturnsProduct() {
        // Given
        given(productRepository.findBySku("SKU-001")).willReturn(Optional.of(validProduct));

        // When
        Optional<Product> result = productService.findBySku("SKU-001");

        // Then
        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("givenNonExistingSku_whenFindBySku_thenReturnsEmpty")
    void givenNonExistingSku_whenFindBySku_thenReturnsEmpty() {
        // Given
        given(productRepository.findBySku("NON-EXISTING")).willReturn(Optional.empty());

        // When
        Optional<Product> result = productService.findBySku("NON-EXISTING");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("givenMultipleProducts_whenFindAll_thenReturnsAllProducts")
    void givenMultipleProducts_whenFindAll_thenReturnsAllProducts() {
        // Given
        Product product2 = new Product(2L, "SKU-002", "Product 2", "Description 2", new BigDecimal("29.99"), 50);
        List<Product> products = Arrays.asList(validProduct, product2);
        given(productRepository.findAll()).willReturn(products);

        // When
        List<Product> result = productService.findAll();

        // Then
        assertThat(result).hasSize(2);
    }
}
