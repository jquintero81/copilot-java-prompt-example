package com.example.application.port.in;

import com.example.application.port.in.command.CreateProductCommand;
import com.example.domain.model.Product;

public interface CreateProductUseCase {
    Product execute(CreateProductCommand command);
}
