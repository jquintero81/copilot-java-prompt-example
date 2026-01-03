package com.example.application.port.in.command;

import java.util.List;

public record CreateOrderCommand(
        Long customerId,
        List<OrderItemCommand> items
) {
}
