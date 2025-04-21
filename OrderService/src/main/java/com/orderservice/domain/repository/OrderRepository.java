package com.orderservice.domain.repository;

import com.orderservice.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order createOrder(Order order);
    Order updateOrder(Order order);
    void deleteOrder(String orderId);
    Optional<Order> findByOrderId(String orderId);
    List<Order> findAll();
}
