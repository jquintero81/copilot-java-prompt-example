package com.example.adapters.inbound.rest;

import com.example.adapters.inbound.rest.dto.ProductRequest;
import com.example.application.port.in.command.CreateProductCommand;
import com.example.application.port.in.CreateProductUseCase;
import com.example.application.port.in.GetProductUseCase;
import com.example.domain.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@DisplayName("ProductController Integration Tests")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateProductUseCase createProductUseCase;

    @MockBean
    private GetProductUseCase getProductUseCase;

    private Product validProduct;
    private ProductRequest validRequest;

    @BeforeEach
    void setUp() {
        validProduct = new Product(
                1L,
                "SKU-001",
                "Test Product",
                "Test Description",
                new BigDecimal("19.99"),
                100
        );

        validRequest = new ProductRequest(
                "SKU-001",
                "Test Product",
                "Test Description",
                new BigDecimal("19.99"),
                100
        );
    }

    @Test
    @DisplayName("givenValidRequest_whenCreateProduct_thenReturns201")
    void givenValidRequest_whenCreateProduct_thenReturns201() throws Exception {
        // Given
        given(createProductUseCase.execute(any(CreateProductCommand.class)))
                .willReturn(validProduct);

        // When & Then
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("givenValidRequest_whenCreateProduct_thenReturnsProductWithCorrectSku")
    void givenValidRequest_whenCreateProduct_thenReturnsProductWithCorrectSku() throws Exception {
        // Given
        given(createProductUseCase.execute(any(CreateProductCommand.class)))
                .willReturn(validProduct);

        // When & Then
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(jsonPath("$.sku").value("SKU-001"));
    }

    @Test
    @DisplayName("givenMissingSku_whenCreateProduct_thenReturns400")
    void givenMissingSku_whenCreateProduct_thenReturns400() throws Exception {
        // Given
        ProductRequest invalidRequest = new ProductRequest(
                "",
                "Test Product",
                "Test Description",
                new BigDecimal("19.99"),
                100
        );

        // When & Then
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenNegativePrice_whenCreateProduct_thenReturns400")
    void givenNegativePrice_whenCreateProduct_thenReturns400() throws Exception {
        // Given
        ProductRequest invalidRequest = new ProductRequest(
                "SKU-001",
                "Test Product",
                "Test Description",
                new BigDecimal("-10.00"),
                100
        );

        // When & Then
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenExistingProductId_whenGetById_thenReturns200")
    void givenExistingProductId_whenGetById_thenReturns200() throws Exception {
        // Given
        given(getProductUseCase.findById(1L)).willReturn(Optional.of(validProduct));

        // When & Then
        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("givenExistingProductId_whenGetById_thenReturnsCorrectProduct")
    void givenExistingProductId_whenGetById_thenReturnsCorrectProduct() throws Exception {
        // Given
        given(getProductUseCase.findById(1L)).willReturn(Optional.of(validProduct));

        // When & Then
        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("givenNonExistingProductId_whenGetById_thenReturns404")
    void givenNonExistingProductId_whenGetById_thenReturns404() throws Exception {
        // Given
        given(getProductUseCase.findById(999L)).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("givenExistingSku_whenGetBySku_thenReturns200")
    void givenExistingSku_whenGetBySku_thenReturns200() throws Exception {
        // Given
        given(getProductUseCase.findBySku("SKU-001")).willReturn(Optional.of(validProduct));

        // When & Then
        mockMvc.perform(get("/api/v1/products/sku/SKU-001"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("givenExistingSku_whenGetBySku_thenReturnsCorrectProduct")
    void givenExistingSku_whenGetBySku_thenReturnsCorrectProduct() throws Exception {
        // Given
        given(getProductUseCase.findBySku("SKU-001")).willReturn(Optional.of(validProduct));

        // When & Then
        mockMvc.perform(get("/api/v1/products/sku/SKU-001"))
                .andExpect(jsonPath("$.sku").value("SKU-001"));
    }

    @Test
    @DisplayName("givenNonExistingSku_whenGetBySku_thenReturns404")
    void givenNonExistingSku_whenGetBySku_thenReturns404() throws Exception {
        // Given
        given(getProductUseCase.findBySku("NON-EXISTING")).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/products/sku/NON-EXISTING"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("givenMultipleProducts_whenGetAll_thenReturns200")
    void givenMultipleProducts_whenGetAll_thenReturns200() throws Exception {
        // Given
        Product product2 = new Product(2L, "SKU-002", "Product 2", "Description 2", new BigDecimal("29.99"), 50);
        List<Product> products = Arrays.asList(validProduct, product2);
        given(getProductUseCase.findAll()).willReturn(products);

        // When & Then
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("givenMultipleProducts_whenGetAll_thenReturnsCorrectCount")
    void givenMultipleProducts_whenGetAll_thenReturnsCorrectCount() throws Exception {
        // Given
        Product product2 = new Product(2L, "SKU-002", "Product 2", "Description 2", new BigDecimal("29.99"), 50);
        List<Product> products = Arrays.asList(validProduct, product2);
        given(getProductUseCase.findAll()).willReturn(products);

        // When & Then
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(jsonPath("$.length()").value(2));
    }
}
