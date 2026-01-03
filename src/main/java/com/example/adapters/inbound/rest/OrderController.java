package com.example.adapters.inbound.rest;

import com.example.adapters.inbound.rest.dto.OrderItemResponse;
import com.example.adapters.inbound.rest.dto.OrderRequest;
import com.example.adapters.inbound.rest.dto.OrderResponse;
import com.example.application.port.in.CreateOrderUseCase;
import com.example.application.port.in.command.CreateOrderCommand;
import com.example.application.port.in.command.OrderItemCommand;
import com.example.domain.model.Order;
import com.example.domain.model.OrderItem;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        List<OrderItemCommand> itemRequests = request.items().stream()
                .map(item -> new OrderItemCommand(item.productId(), item.quantity()))
                .collect(Collectors.toList());

        CreateOrderCommand command = new CreateOrderCommand(
                request.customerId(),
                itemRequests
        );
        Order order = createOrderUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(order));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                items,
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}
