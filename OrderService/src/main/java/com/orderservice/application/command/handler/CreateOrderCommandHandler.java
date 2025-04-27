package com.orderservice.application.command.handler;

import com.orderservice.application.command.model.CreateOrderCommand;
import com.orderservice.application.command.model.CreateOrderEvent;
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
    private final OrderPublisher orderPublisher;

    public Order handle(CreateOrderCommand command) {
        // 1) Zbuduj domain Order
        List<OrderItem> items = command.getItems().stream()
                .map(i -> OrderItem.builder()
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
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

        // 2) Zapisz zamówienie w repozytorium
        orderRepository.createOrder(order);

        // 3) Zbuduj i wyślij event CreateOrderEvent
        CreateOrderEvent event = CreateOrderEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .items(items.stream()
                        .map(i -> CreateOrderEvent.OrderItem.builder()
                                .productId(i.getProductId())
                                .quantity(i.getQuantity())
                                .build())
                        .toList())
                .build();

        orderPublisher.publishCreateOrder(event);

        return order;
    }
}
