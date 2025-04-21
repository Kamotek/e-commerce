package com.orderservice.infrastructure.controller;

import com.orderservice.application.command.model.CreateOrderCommand;
import com.orderservice.application.command.handler.CreateOrderCommandHandler;
import com.orderservice.domain.model.Order;
import com.orderservice.domain.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderCommandHandler createHandler;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        Order created = createHandler.handle(command);
        URI location = URI.create("/api/orders/" + created.getId());
        return ResponseEntity
                .created(location)
                .body(created);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID orderId) {
        return orderRepository.findByOrderId(orderId.toString())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Order>> listOrders() {
        List<Order> all = orderRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody Order order) {

        order.setId(orderId);
        Order updated = orderRepository.updateOrder(order);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        orderRepository.deleteOrder(orderId.toString());
        return ResponseEntity.noContent().build();
    }
}
