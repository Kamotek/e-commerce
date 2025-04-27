package com.bffservice.interfaces.rest;


import com.bffservice.application.command.model.CreateOrderCommand;
import com.bffservice.domain.model.Order;
import com.bffservice.domain.model.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order-service", url = "${order.service.url}")
public interface OrderServiceClient {

    @PostMapping("/api/orders")
    ResponseEntity<Order> createOrder(@RequestBody CreateOrderCommand command);

    @GetMapping("/api/orders/{orderId}")
    ResponseEntity<Order> getOrder(@PathVariable("orderId") UUID orderId);

    @GetMapping("/api/orders")
    ResponseEntity<List<Order>> listOrders();

    @PutMapping("/api/orders/{orderId}")
    ResponseEntity<Order> updateOrder(@PathVariable("orderId") UUID orderId,
                                         @RequestBody Order order);

    @DeleteMapping("/api/orders/{orderId}")
    ResponseEntity<Void> deleteOrder(@PathVariable("orderId") UUID orderId);
}
