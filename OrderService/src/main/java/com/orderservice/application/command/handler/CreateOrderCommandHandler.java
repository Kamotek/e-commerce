package com.orderservice.application.command.handler;

import com.orderservice.application.command.model.CreateOrderCommand;
import com.orderservice.application.mapper.OrderMapper;
import com.orderservice.domain.model.Order;
import com.orderservice.domain.model.OrderItem;
import com.orderservice.domain.repository.OrderRepository;
import com.orderservice.infrastructure.messaging.producer.OrderPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class CreateOrderCommandHandler {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderPublisher orderPublisher;

    public Order handle(CreateOrderCommand command) {
        List<OrderItem> items = command.getItems().stream()
                .map(item -> OrderItem.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .build())
                .toList();

        Order order = Order.builder()
                .id(UUID.randomUUID())
                .userId(command.getUserId())
                .orderDate(
                        command.getOrderDate() != null
                                ? command.getOrderDate()
                                : Instant.now()
                )
                .items(items)
                .finished(false)
                .build();

        orderPublisher.publishCreateOrder(command);
        return orderRepository.createOrder(order);
    }
}
