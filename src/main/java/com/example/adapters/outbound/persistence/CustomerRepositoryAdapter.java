package com.example.adapters.outbound.persistence;

import com.example.application.port.out.CustomerRepositoryPort;
import com.example.domain.model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final SpringDataCustomerRepository springDataCustomerRepository;

    public CustomerRepositoryAdapter(SpringDataCustomerRepository springDataCustomerRepository) {
        this.springDataCustomerRepository = springDataCustomerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = toEntity(customer);
        CustomerEntity saved = springDataCustomerRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return springDataCustomerRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return springDataCustomerRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return springDataCustomerRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        springDataCustomerRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataCustomerRepository.existsByEmail(email);
    }

    private CustomerEntity toEntity(Customer customer) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(customer.getId());
        entity.setEmail(customer.getEmail());
        entity.setFirstName(customer.getFirstName());
        entity.setLastName(customer.getLastName());
        entity.setPhone(customer.getPhone());
        entity.setAddress(customer.getAddress());
        entity.setCreatedAt(customer.getCreatedAt());
        entity.setUpdatedAt(customer.getUpdatedAt());
        return entity;
    }

    private Customer toDomain(CustomerEntity entity) {
        Customer customer = new Customer(
                entity.getId(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getAddress()
        );
        customer.setCreatedAt(entity.getCreatedAt());
        customer.setUpdatedAt(entity.getUpdatedAt());
        return customer;
    }
}
