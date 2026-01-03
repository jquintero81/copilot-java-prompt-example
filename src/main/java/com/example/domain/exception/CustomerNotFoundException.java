package com.example.domain.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long customerId) {
        super("Customer not found with id: " + customerId);
    }

    public CustomerNotFoundException(String email) {
        super("Customer not found with email: " + email);
    }
}
