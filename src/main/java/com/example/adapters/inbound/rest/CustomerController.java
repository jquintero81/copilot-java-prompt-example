package com.example.adapters.inbound.rest;

import com.example.adapters.inbound.rest.dto.CustomerRequest;
import com.example.adapters.inbound.rest.dto.CustomerResponse;
import com.example.application.port.in.CreateCustomerUseCase;
import com.example.application.port.in.command.CreateCustomerCommand;
import com.example.domain.model.Customer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CreateCustomerCommand command = new CreateCustomerCommand(
                request.email(),
                request.firstName(),
                request.lastName(),
                request.phone(),
                request.address()
        );
        Customer customer = createCustomerUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(customer));
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getEmail(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
