package com.orderservice.domain.repository;

import com.orderservice.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order createOrder(Order order);
    Order updateOrder(Order order);
    void deleteOrder(String orderId);
    Optional<Order> findByOrderId(UUID orderId);
    List<Order> findAll();
}
