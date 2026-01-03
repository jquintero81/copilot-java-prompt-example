package com.example.application.service;

import com.example.application.port.in.command.CreateCustomerCommand;
import com.example.application.port.in.CreateCustomerUseCase;
import com.example.application.port.out.CustomerRepositoryPort;
import com.example.domain.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements CreateCustomerUseCase {

    private final CustomerRepositoryPort customerRepository;

    public CustomerService(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer execute(CreateCustomerCommand command) {
        validateCustomerCommand(command);

        if (customerRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Customer with email " + command.email() + " already exists");
        }

        Customer customer = new Customer(
                null,
                command.email(),
                command.firstName(),
                command.lastName(),
                command.phone(),
                command.address()
        );

        return customerRepository.save(customer);
    }

    private void validateCustomerCommand(CreateCustomerCommand command) {
        if (command.email() == null || command.email().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (command.firstName() == null || command.firstName().isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (command.lastName() == null || command.lastName().isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
    }
}
