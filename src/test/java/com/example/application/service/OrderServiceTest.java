package com.example.application.service;

import com.example.application.port.in.command.CreateOrderCommand;
import com.example.application.port.in.command.OrderItemCommand;
import com.example.application.port.out.CustomerRepositoryPort;
import com.example.application.port.out.OrderRepositoryPort;
import com.example.application.port.out.ProductRepositoryPort;
import com.example.domain.exception.CustomerNotFoundException;
import com.example.domain.exception.InsufficientStockException;
import com.example.domain.exception.ProductNotFoundException;
import com.example.domain.model.Customer;
import com.example.domain.model.Order;
import com.example.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Tests")
class OrderServiceTest {

    @Mock
    private OrderRepositoryPort orderRepository;

    @Mock
    private CustomerRepositoryPort customerRepository;

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private OrderService orderService;

    private Customer validCustomer;
    private Product validProduct;
    private CreateOrderCommand validCommand;
    private Order validOrder;

    @BeforeEach
    void setUp() {
        validCustomer = new Customer(
                1L,
                "test@example.com",
                "John",
                "Doe",
                "+1234567890",
                "123 Main St"
        );

        validProduct = new Product(
                1L,
                "SKU-001",
                "Test Product",
                "Test Description",
                new BigDecimal("19.99"),
                100
        );

        OrderItemCommand itemRequest = new OrderItemCommand(1L, 5);
        validCommand = new CreateOrderCommand(1L, List.of(itemRequest));

        validOrder = new Order(1L, 1L);
    }

    @Test
    @DisplayName("givenValidCommand_whenCreateOrder_thenOrderIsSaved")
    void givenValidCommand_whenCreateOrder_thenOrderIsSaved() {
        // Given
        given(customerRepository.findById(1L)).willReturn(Optional.of(validCustomer));
        given(productRepository.findById(1L)).willReturn(Optional.of(validProduct));
        given(orderRepository.save(any(Order.class))).willReturn(validOrder);

        // When
        Order result = orderService.execute(validCommand);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("givenValidCommand_whenCreateOrder_thenProductStockIsReduced")
    void givenValidCommand_whenCreateOrder_thenProductStockIsReduced() {
        // Given
        given(customerRepository.findById(1L)).willReturn(Optional.of(validCustomer));
        given(productRepository.findById(1L)).willReturn(Optional.of(validProduct));
        given(orderRepository.save(any(Order.class))).willReturn(validOrder);

        // When
        orderService.execute(validCommand);

        // Then
        then(productRepository).should(times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("givenInvalidCustomerId_whenCreateOrder_thenThrowsCustomerNotFoundException")
    void givenInvalidCustomerId_whenCreateOrder_thenThrowsCustomerNotFoundException() {
        // Given
        given(customerRepository.findById(999L)).willReturn(Optional.empty());
        OrderItemCommand itemRequest = new OrderItemCommand(1L, 5);
        CreateOrderCommand invalidCommand = new CreateOrderCommand(999L, List.of(itemRequest));

        // When & Then
        assertThatThrownBy(() -> orderService.execute(invalidCommand))
                .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    @DisplayName("givenInvalidProductId_whenCreateOrder_thenThrowsProductNotFoundException")
    void givenInvalidProductId_whenCreateOrder_thenThrowsProductNotFoundException() {
        // Given
        given(customerRepository.findById(1L)).willReturn(Optional.of(validCustomer));
        given(productRepository.findById(999L)).willReturn(Optional.empty());
        OrderItemCommand itemRequest = new OrderItemCommand(999L, 5);
        CreateOrderCommand invalidCommand = new CreateOrderCommand(1L, List.of(itemRequest));

        // When & Then
        assertThatThrownBy(() -> orderService.execute(invalidCommand))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("givenInsufficientStock_whenCreateOrder_thenThrowsInsufficientStockException")
    void givenInsufficientStock_whenCreateOrder_thenThrowsInsufficientStockException() {
        // Given
        given(customerRepository.findById(1L)).willReturn(Optional.of(validCustomer));
        given(productRepository.findById(1L)).willReturn(Optional.of(validProduct));
        OrderItemCommand itemRequest = new OrderItemCommand(1L, 200); // More than available stock
        CreateOrderCommand invalidCommand = new CreateOrderCommand(1L, List.of(itemRequest));

        // When & Then
        assertThatThrownBy(() -> orderService.execute(invalidCommand))
                .isInstanceOf(InsufficientStockException.class);
    }

    @Test
    @DisplayName("givenNullCustomerId_whenCreateOrder_thenThrowsException")
    void givenNullCustomerId_whenCreateOrder_thenThrowsException() {
        // Given
        OrderItemCommand itemRequest = new OrderItemCommand(1L, 5);
        CreateOrderCommand invalidCommand = new CreateOrderCommand(null, List.of(itemRequest));

        // When & Then
        assertThatThrownBy(() -> orderService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer ID cannot be null");
    }

    @Test
    @DisplayName("givenEmptyItems_whenCreateOrder_thenThrowsException")
    void givenEmptyItems_whenCreateOrder_thenThrowsException() {
        // Given
        CreateOrderCommand invalidCommand = new CreateOrderCommand(1L, Collections.emptyList());

        // When & Then
        assertThatThrownBy(() -> orderService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must have at least one item");
    }

    @Test
    @DisplayName("givenNullItems_whenCreateOrder_thenThrowsException")
    void givenNullItems_whenCreateOrder_thenThrowsException() {
        // Given
        CreateOrderCommand invalidCommand = new CreateOrderCommand(1L, null);

        // When & Then
        assertThatThrownBy(() -> orderService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must have at least one item");
    }

    @Test
    @DisplayName("givenNullProductId_whenCreateOrder_thenThrowsException")
    void givenNullProductId_whenCreateOrder_thenThrowsException() {
        // Given
        OrderItemCommand itemRequest = new OrderItemCommand(null, 5);
        CreateOrderCommand invalidCommand = new CreateOrderCommand(1L, List.of(itemRequest));

        // When & Then
        assertThatThrownBy(() -> orderService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product ID cannot be null");
    }

    @Test
    @DisplayName("givenZeroQuantity_whenCreateOrder_thenThrowsException")
    void givenZeroQuantity_whenCreateOrder_thenThrowsException() {
        // Given
        OrderItemCommand itemRequest = new OrderItemCommand(1L, 0);
        CreateOrderCommand invalidCommand = new CreateOrderCommand(1L, List.of(itemRequest));

        // When & Then
        assertThatThrownBy(() -> orderService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be positive");
    }

    @Test
    @DisplayName("givenNegativeQuantity_whenCreateOrder_thenThrowsException")
    void givenNegativeQuantity_whenCreateOrder_thenThrowsException() {
        // Given
        OrderItemCommand itemRequest = new OrderItemCommand(1L, -5);
        CreateOrderCommand invalidCommand = new CreateOrderCommand(1L, List.of(itemRequest));

        // When & Then
        assertThatThrownBy(() -> orderService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be positive");
    }
}
