package com.example.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "SKU is required") String sku,
        @NotBlank(message = "Name is required") String name,
        String description,
        @NotNull(message = "Price is required") @Positive(message = "Price must be positive") BigDecimal price,
        @NotNull(message = "Stock quantity is required") @PositiveOrZero(message = "Stock quantity cannot be negative") Integer stockQuantity
) {
}
