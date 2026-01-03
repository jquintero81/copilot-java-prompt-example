package com.example.application.port.in.command;

import java.math.BigDecimal;

public record CreateProductCommand(
        String sku,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity
) {
}
