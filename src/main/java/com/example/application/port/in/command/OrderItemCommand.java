package com.example.application.port.in.command;

public record OrderItemCommand(
        Long productId,
        Integer quantity
) {
}
