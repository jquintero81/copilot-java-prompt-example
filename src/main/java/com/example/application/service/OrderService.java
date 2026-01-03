package com.example.application.service;

import com.example.application.port.in.command.CreateOrderCommand;
import com.example.application.port.in.CreateOrderUseCase;
import com.example.application.port.in.command.OrderItemCommand;
import com.example.application.port.out.CustomerRepositoryPort;
import com.example.application.port.out.OrderRepositoryPort;
import com.example.application.port.out.ProductRepositoryPort;
import com.example.domain.exception.CustomerNotFoundException;
import com.example.domain.exception.InsufficientStockException;
import com.example.domain.exception.ProductNotFoundException;
import com.example.domain.model.Customer;
import com.example.domain.model.Order;
import com.example.domain.model.OrderItem;
import com.example.domain.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final CustomerRepositoryPort customerRepository;
    private final ProductRepositoryPort productRepository;

    public OrderService(OrderRepositoryPort orderRepository,
                        CustomerRepositoryPort customerRepository,
                        ProductRepositoryPort productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Order execute(CreateOrderCommand command) {
        validateOrderCommand(command);

        Customer customer = customerRepository.findById(command.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(command.customerId()));

        Order order = new Order(null, customer.getId());

        for (OrderItemCommand itemRequest : command.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ProductNotFoundException(itemRequest.productId()));

            if (!product.isAvailable(itemRequest.quantity())) {
                throw new InsufficientStockException(
                        product.getName(),
                        itemRequest.quantity(),
                        product.getStockQuantity()
                );
            }

            product.reduceStock(itemRequest.quantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(
                    null,
                    null,
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    itemRequest.quantity()
            );

            order.addItem(orderItem);
        }

        return orderRepository.save(order);
    }

    private void validateOrderCommand(CreateOrderCommand command) {
        if (command.customerId() == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        if (command.items() == null || command.items().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        for (OrderItemCommand item : command.items()) {
            if (item.productId() == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            if (item.quantity() == null || item.quantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be positive");
            }
        }
    }
}
