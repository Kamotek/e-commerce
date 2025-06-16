package com.orderservice.infrastructure.persistence.repository;

import com.orderservice.application.mapper.OrderMapper;
import com.orderservice.domain.model.Order;
import com.orderservice.domain.repository.OrderRepository;
import com.orderservice.infrastructure.persistence.entity.OrderEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpringDataOrderRepository implements OrderRepository {
    private final OrderJpaRepository jpa;
    private final OrderMapper mapper;

    @Override
    public Order createOrder(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        entity.setItems(mapper.mapItemsToEntity(order.getItems(), entity));
        OrderEntity saved = jpa.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Order updateOrder(Order order) {
        OrderEntity existingEntity = jpa.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + order.getId()));

        mapper.updateEntityFromDomain(order, existingEntity);

        OrderEntity saved = jpa.save(existingEntity);

        return mapper.toDomain(saved);
    }

    @Override
    public void deleteOrder(String orderId) {
        jpa.deleteById(UUID.fromString(orderId));
    }

    @Override
    public Optional<Order> findByOrderId(UUID orderId) {
        return jpa.findById(orderId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return jpa.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
