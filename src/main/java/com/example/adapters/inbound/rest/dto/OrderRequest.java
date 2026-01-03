package com.example.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull(message = "Customer ID is required") Long customerId,
        @NotEmpty(message = "Order must have at least one item") List<OrderItemRequest> items
) {
}
