package com.example.application.service;

import com.example.application.port.in.command.CreateCustomerCommand;
import com.example.application.port.out.CustomerRepositoryPort;
import com.example.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private CreateCustomerCommand validCommand;
    private Customer validCustomer;

    @BeforeEach
    void setUp() {
        validCommand = new CreateCustomerCommand(
                "test@example.com",
                "John",
                "Doe",
                "+1234567890",
                "123 Main St, City, Country"
        );
        
        validCustomer = new Customer(
                1L,
                "test@example.com",
                "John",
                "Doe",
                "+1234567890",
                "123 Main St, City, Country"
        );
    }

    @Test
    @DisplayName("givenValidCommand_whenCreateCustomer_thenCustomerIsSaved")
    void givenValidCommand_whenCreateCustomer_thenCustomerIsSaved() {
        // Given
        given(customerRepository.existsByEmail(validCommand.email())).willReturn(false);
        given(customerRepository.save(any(Customer.class))).willReturn(validCustomer);

        // When
        Customer result = customerService.execute(validCommand);

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("givenValidCommand_whenCreateCustomer_thenCustomerHasCorrectEmail")
    void givenValidCommand_whenCreateCustomer_thenCustomerHasCorrectEmail() {
        // Given
        given(customerRepository.existsByEmail(validCommand.email())).willReturn(false);
        given(customerRepository.save(any(Customer.class))).willReturn(validCustomer);

        // When
        Customer result = customerService.execute(validCommand);

        // Then
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("givenValidCommand_whenCreateCustomer_thenCustomerHasCorrectName")
    void givenValidCommand_whenCreateCustomer_thenCustomerHasCorrectName() {
        // Given
        given(customerRepository.existsByEmail(validCommand.email())).willReturn(false);
        given(customerRepository.save(any(Customer.class))).willReturn(validCustomer);

        // When
        Customer result = customerService.execute(validCommand);

        // Then
        assertThat(result.getFirstName()).isEqualTo("John");
    }

    @Test
    @DisplayName("givenDuplicateEmail_whenCreateCustomer_thenThrowsException")
    void givenDuplicateEmail_whenCreateCustomer_thenThrowsException() {
        // Given
        given(customerRepository.existsByEmail(validCommand.email())).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> customerService.execute(validCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("givenDuplicateEmail_whenCreateCustomer_thenCustomerIsNotSaved")
    void givenDuplicateEmail_whenCreateCustomer_thenCustomerIsNotSaved() {
        // Given
        given(customerRepository.existsByEmail(validCommand.email())).willReturn(true);

        // When & Then
        try {
            customerService.execute(validCommand);
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        then(customerRepository).should(never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("givenNullEmail_whenCreateCustomer_thenThrowsException")
    void givenNullEmail_whenCreateCustomer_thenThrowsException() {
        // Given
        CreateCustomerCommand invalidCommand = new CreateCustomerCommand(
                null,
                "John",
                "Doe",
                "+1234567890",
                "123 Main St"
        );

        // When & Then
        assertThatThrownBy(() -> customerService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email cannot be empty");
    }

    @Test
    @DisplayName("givenBlankEmail_whenCreateCustomer_thenThrowsException")
    void givenBlankEmail_whenCreateCustomer_thenThrowsException() {
        // Given
        CreateCustomerCommand invalidCommand = new CreateCustomerCommand(
                "   ",
                "John",
                "Doe",
                "+1234567890",
                "123 Main St"
        );

        // When & Then
        assertThatThrownBy(() -> customerService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email cannot be empty");
    }

    @Test
    @DisplayName("givenNullFirstName_whenCreateCustomer_thenThrowsException")
    void givenNullFirstName_whenCreateCustomer_thenThrowsException() {
        // Given
        CreateCustomerCommand invalidCommand = new CreateCustomerCommand(
                "test@example.com",
                null,
                "Doe",
                "+1234567890",
                "123 Main St"
        );

        // When & Then
        assertThatThrownBy(() -> customerService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name cannot be empty");
    }

    @Test
    @DisplayName("givenBlankFirstName_whenCreateCustomer_thenThrowsException")
    void givenBlankFirstName_whenCreateCustomer_thenThrowsException() {
        // Given
        CreateCustomerCommand invalidCommand = new CreateCustomerCommand(
                "test@example.com",
                "   ",
                "Doe",
                "+1234567890",
                "123 Main St"
        );

        // When & Then
        assertThatThrownBy(() -> customerService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First name cannot be empty");
    }

    @Test
    @DisplayName("givenNullLastName_whenCreateCustomer_thenThrowsException")
    void givenNullLastName_whenCreateCustomer_thenThrowsException() {
        // Given
        CreateCustomerCommand invalidCommand = new CreateCustomerCommand(
                "test@example.com",
                "John",
                null,
                "+1234567890",
                "123 Main St"
        );

        // When & Then
        assertThatThrownBy(() -> customerService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last name cannot be empty");
    }

    @Test
    @DisplayName("givenBlankLastName_whenCreateCustomer_thenThrowsException")
    void givenBlankLastName_whenCreateCustomer_thenThrowsException() {
        // Given
        CreateCustomerCommand invalidCommand = new CreateCustomerCommand(
                "test@example.com",
                "John",
                "   ",
                "+1234567890",
                "123 Main St"
        );

        // When & Then
        assertThatThrownBy(() -> customerService.execute(invalidCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Last name cannot be empty");
    }
}
