package com.example.application.port.in;

import com.example.application.port.in.command.CreateOrderCommand;
import com.example.domain.model.Order;

public interface CreateOrderUseCase {
    Order execute(CreateOrderCommand command);
}
