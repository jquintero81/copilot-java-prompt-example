package com.example.adapters.inbound.rest;

import com.example.adapters.inbound.rest.dto.CustomerRequest;
import com.example.application.port.in.command.CreateCustomerCommand;
import com.example.application.port.in.CreateCustomerUseCase;
import com.example.domain.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
@DisplayName("CustomerController Integration Tests")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateCustomerUseCase createCustomerUseCase;

    private Customer validCustomer;
    private CustomerRequest validRequest;

    @BeforeEach
    void setUp() {
        validCustomer = new Customer(
                1L,
                "test@example.com",
                "John",
                "Doe",
                "+1234567890",
                "123 Main St, City, Country"
        );

        validRequest = new CustomerRequest(
                "test@example.com",
                "John",
                "Doe",
                "+1234567890",
                "123 Main St, City, Country"
        );
    }

    @Test
    @DisplayName("givenValidRequest_whenCreateCustomer_thenReturns201")
    void givenValidRequest_whenCreateCustomer_thenReturns201() throws Exception {
        // Given
        given(createCustomerUseCase.execute(any(CreateCustomerCommand.class)))
                .willReturn(validCustomer);

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("givenValidRequest_whenCreateCustomer_thenReturnsCustomerWithCorrectEmail")
    void givenValidRequest_whenCreateCustomer_thenReturnsCustomerWithCorrectEmail() throws Exception {
        // Given
        given(createCustomerUseCase.execute(any(CreateCustomerCommand.class)))
                .willReturn(validCustomer);

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("givenValidRequest_whenCreateCustomer_thenReturnsCustomerWithCorrectName")
    void givenValidRequest_whenCreateCustomer_thenReturnsCustomerWithCorrectName() throws Exception {
        // Given
        given(createCustomerUseCase.execute(any(CreateCustomerCommand.class)))
                .willReturn(validCustomer);

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("givenMissingEmail_whenCreateCustomer_thenReturns400")
    void givenMissingEmail_whenCreateCustomer_thenReturns400() throws Exception {
        // Given
        CustomerRequest invalidRequest = new CustomerRequest(
                "",
                "John",
                "Doe",
                "+1234567890",
                "123 Main St"
        );

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenInvalidEmail_whenCreateCustomer_thenReturns400")
    void givenInvalidEmail_whenCreateCustomer_thenReturns400() throws Exception {
        // Given
        CustomerRequest invalidRequest = new CustomerRequest(
                "not-an-email",
                "John",
                "Doe",
                "+1234567890",
                "123 Main St"
        );

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenMissingFirstName_whenCreateCustomer_thenReturns400")
    void givenMissingFirstName_whenCreateCustomer_thenReturns400() throws Exception {
        // Given
        CustomerRequest invalidRequest = new CustomerRequest(
                "test@example.com",
                "",
                "Doe",
                "+1234567890",
                "123 Main St"
        );

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenMissingLastName_whenCreateCustomer_thenReturns400")
    void givenMissingLastName_whenCreateCustomer_thenReturns400() throws Exception {
        // Given
        CustomerRequest invalidRequest = new CustomerRequest(
                "test@example.com",
                "John",
                "",
                "+1234567890",
                "123 Main St"
        );

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("givenOptionalFieldsNull_whenCreateCustomer_thenReturns201")
    void givenOptionalFieldsNull_whenCreateCustomer_thenReturns201() throws Exception {
        // Given
        CustomerRequest requestWithNulls = new CustomerRequest(
                "test@example.com",
                "John",
                "Doe",
                null,
                null
        );
        Customer customerWithNulls = new Customer(1L, "test@example.com", "John", "Doe", null, null);
        given(createCustomerUseCase.execute(any(CreateCustomerCommand.class)))
                .willReturn(customerWithNulls);

        // When & Then
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithNulls)))
                .andExpect(status().isCreated());
    }
}
