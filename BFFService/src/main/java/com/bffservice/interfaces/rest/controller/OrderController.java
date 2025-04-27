package com.bffservice.interfaces.rest.controller;


import com.bffservice.application.command.model.CreateOrderCommand;
import com.bffservice.domain.model.Order;
import com.bffservice.interfaces.rest.OrderServiceClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceClient orderClient;

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @Valid @RequestBody CreateOrderCommand command
    ) {
        ResponseEntity<Order> resp = orderClient.createOrder(command);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resp.getBody().getId())
                .toUri();
        return ResponseEntity.created(location).body(resp.getBody());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID orderId) {
        ResponseEntity<Order> resp = orderClient.getOrder(orderId);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @GetMapping
    public ResponseEntity<List<Order>> listOrders() {
        ResponseEntity<List<Order>> resp = orderClient.listOrders();
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody Order order
    ) {
        ResponseEntity<Order> resp = orderClient.updateOrder(orderId, order);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        ResponseEntity<Void> resp = orderClient.deleteOrder(orderId);
        return ResponseEntity.status(resp.getStatusCode()).build();
    }
}
