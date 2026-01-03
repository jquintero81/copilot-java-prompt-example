package com.example.adapters.outbound.persistence;

import com.example.application.port.out.OrderRepositoryPort;
import com.example.domain.model.Order;
import com.example.domain.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final SpringDataOrderRepository springDataOrderRepository;

    public OrderRepositoryAdapter(SpringDataOrderRepository springDataOrderRepository) {
        this.springDataOrderRepository = springDataOrderRepository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = springDataOrderRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return springDataOrderRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return springDataOrderRepository.findByCustomerId(customerId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findAll() {
        return springDataOrderRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        springDataOrderRepository.deleteById(id);
    }

    private OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setCustomerId(order.getCustomerId());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setStatus(order.getStatus());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());

        for (OrderItem item : order.getItems()) {
            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setId(item.getId());
            itemEntity.setProductId(item.getProductId());
            itemEntity.setProductName(item.getProductName());
            itemEntity.setUnitPrice(item.getUnitPrice());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setSubtotal(item.getSubtotal());
            entity.addItem(itemEntity);
        }

        return entity;
    }

    private Order toDomain(OrderEntity entity) {
        Order order = new Order(entity.getId(), entity.getCustomerId());
        order.setStatus(entity.getStatus());
        order.setCreatedAt(entity.getCreatedAt());
        order.setUpdatedAt(entity.getUpdatedAt());

        List<OrderItem> items = entity.getItems().stream()
                .map(itemEntity -> {
                    OrderItem item = new OrderItem(
                            itemEntity.getId(),
                            entity.getId(),
                            itemEntity.getProductId(),
                            itemEntity.getProductName(),
                            itemEntity.getUnitPrice(),
                            itemEntity.getQuantity()
                    );
                    return item;
                })
                .collect(Collectors.toList());

        order.setItems(items);
        return order;
    }
}
