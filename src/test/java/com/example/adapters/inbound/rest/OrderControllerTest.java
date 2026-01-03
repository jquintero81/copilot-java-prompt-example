package com.example.adapters.inbound.rest;

import com.example.adapters.inbound.rest.dto.OrderItemRequest;
import com.example.adapters.inbound.rest.dto.OrderRequest;
import com.example.application.port.in.command.CreateOrderCommand;
import com.example.application.port.in.CreateOrderUseCase;
import com.example.domain.exception.CustomerNotFoundException;
import com.example.domain.exception.InsufficientStockException;
import com.example.domain.exception.ProductNotFoundException;
import com.example.domain.model.Order;
import com.example.domain.model.OrderItem;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@DisplayName("OrderController Integration Tests")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    private Order validOrder;
    private OrderRequest validRequest;

    @BeforeEach
    void setUp() {
        OrderItem orderItem = new OrderItem(
                1L,
                1L,
                1L,
                "Test Product",
                new BigDecimal("19.99"),
                5
        );
        
        validOrder = new Order(1L, 1L);
        validOrder.addItem(orderItem);

        OrderItemRequest itemRequest = new OrderItemRequest(1L, 5);
        validRequest = new OrderRequest(1L, List.of(itemRequest));
    }

    @Test
    @DisplayName("givenValidRequest_whenCreateOrder_thenReturns201")
    void givenValidRequest_whenCreateOrder_thenReturns201() throws Exception {
        // Given
        given(createOrderUseCase.execute(any(CreateOrderCommand.class)))
                .willReturn(validOrder);

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("givenValidRequest_whenCreateOrder_thenReturnsOrderWithCorrectCustomerId")
    void givenValidRequest_whenCreateOrder_thenReturnsOrderWithCorrectCustomerId() throws Exception {
        // Given
        given(createOrderUseCase.execute(any(CreateOrderCommand.class)))
                .willReturn(validOrder);

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(jsonPath("$.customerId").value(1L));
    }

    @Test
    @DisplayName("givenValidRequest_whenCreateOrder_thenReturnsOrderWithItems")
    void givenValidRequest_whenCreateOrder_thenReturnsOrderWithItems() throws Exception {
        // Given
        given(createOrderUseCase.execute(any(CreateOrderCommand.class)))
                .willReturn(validOrder);

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    @DisplayName("givenMissingCustomerId_whenCreateOrder_thenReturns400")
    void givenMissingCustomerId_whenCreateOrder_thenReturns400() throws Exception {
        // Given
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 5);
        OrderRequest invalidRequest = new OrderRequest(null, List.of(itemRequest));

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenEmptyItems_whenCreateOrder_thenReturns400")
    void givenEmptyItems_whenCreateOrder_thenReturns400() throws Exception {
        // Given
        OrderRequest invalidRequest = new OrderRequest(1L, Collections.emptyList());

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenNonExistingCustomer_whenCreateOrder_thenReturns404")
    void givenNonExistingCustomer_whenCreateOrder_thenReturns404() throws Exception {
        // Given
        given(createOrderUseCase.execute(any(CreateOrderCommand.class)))
                .willThrow(new CustomerNotFoundException(999L));

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("givenNonExistingProduct_whenCreateOrder_thenReturns404")
    void givenNonExistingProduct_whenCreateOrder_thenReturns404() throws Exception {
        // Given
        given(createOrderUseCase.execute(any(CreateOrderCommand.class)))
                .willThrow(new ProductNotFoundException(999L));

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("givenInsufficientStock_whenCreateOrder_thenReturns409")
    void givenInsufficientStock_whenCreateOrder_thenReturns409() throws Exception {
        // Given
        given(createOrderUseCase.execute(any(CreateOrderCommand.class)))
                .willThrow(new InsufficientStockException("Test Product", 100, 50));

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("givenMultipleItems_whenCreateOrder_thenReturns201")
    void givenMultipleItems_whenCreateOrder_thenReturns201() throws Exception {
        // Given
        OrderItem orderItem1 = new OrderItem(1L, 1L, 1L, "Product 1", new BigDecimal("19.99"), 5);
        OrderItem orderItem2 = new OrderItem(2L, 1L, 2L, "Product 2", new BigDecimal("29.99"), 3);
        Order multiItemOrder = new Order(1L, 1L);
        multiItemOrder.addItem(orderItem1);
        multiItemOrder.addItem(orderItem2);
        
        OrderItemRequest item1 = new OrderItemRequest(1L, 5);
        OrderItemRequest item2 = new OrderItemRequest(2L, 3);
        OrderRequest multiItemRequest = new OrderRequest(1L, List.of(item1, item2));
        
        given(createOrderUseCase.execute(any(CreateOrderCommand.class)))
                .willReturn(multiItemOrder);

        // When & Then
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(multiItemRequest)))
                .andExpect(status().isCreated());
    }
}
