package com.example.application.port.in;

import com.example.application.port.in.command.CreateCustomerCommand;
import com.example.domain.model.Customer;

public interface CreateCustomerUseCase {
    Customer execute(CreateCustomerCommand command);
}
