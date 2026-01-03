package com.example.application.port.in.command;

public record CreateCustomerCommand(
        String email,
        String firstName,
        String lastName,
        String phone,
        String address
) {
}
