package com.bffservice.interfaces.rest.controller;

import com.bffservice.application.command.model.CreateOrderCommand;
import com.bffservice.domain.model.Order;
import com.bffservice.domain.model.OrderItem;
import com.bffservice.interfaces.rest.OrderServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceClient orderServiceClient;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResource> getOrder(@PathVariable UUID orderId) {
        ResponseEntity<Order> response = orderServiceClient.getOrder(orderId);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return ResponseEntity.notFound().build();
        }
        Order order = response.getBody();
        OrderResource resource = mapOrderToResource(order);
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    public ResponseEntity<List<OrderResource>> listOrders() {
        ResponseEntity<List<Order>> response = orderServiceClient.listOrders();
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return ResponseEntity.notFound().build();
        }
        List<OrderResource> resources = new ArrayList<>();
        for (Order order : response.getBody()) {
            resources.add(mapOrderToResource(order));
        }
        return ResponseEntity.ok(resources);
    }

@PostMapping
public ResponseEntity<OrderResource> createOrder(@RequestBody CreateOrderCommand command) {
    ResponseEntity<Order> response = orderServiceClient.createOrder(command);
    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
        return ResponseEntity.badRequest().build();
    }
    OrderResource resource = mapOrderToResource(response.getBody());
    return ResponseEntity.ok(resource);
}
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResource> updateOrder(@PathVariable UUID orderId, @RequestBody Order order) {
        ResponseEntity<Order> response = orderServiceClient.updateOrder(orderId, order);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return ResponseEntity.badRequest().build();
        }
        OrderResource resource = mapOrderToResource(response.getBody());
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        ResponseEntity<Void> response = orderServiceClient.deleteOrder(orderId);
        return ResponseEntity.status(response.getStatusCode()).build();
    }

    private OrderResource mapOrderToResource(Order order) {
        OrderResource resource = new OrderResource();
        resource.id = order.getId().toString();
        resource.userId = order.getUserId().toString();
        resource.orderDate = order.getOrderDate();
        resource.shippingStreet = order.getShippingStreet();
        resource.shippingCity = order.getShippingCity();
        resource.shippingPostalCode = order.getShippingPostalCode();
        resource.shippingCountry = order.getShippingCountry();
        resource.paymentStatus = order.getPaymentStatus();
        resource.finished = order.isFinished();
        resource.totalAmount = order.getTotalAmount();
        resource.items = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderResource.Item resItem = new OrderResource.Item();
            resItem.productId = item.getProductId().toString();
            resItem.quantity = item.getQuantity();
            resource.items.add(resItem);
        }
        resource.links = new ArrayList<>();
        resource.links.add(new OrderResource.Link("self", "/api/orders/" + resource.id, "GET"));
        if ("PENDING".equals(order.getPaymentStatus())) {
            resource.links.add(new OrderResource.Link("pay", "/api/payments/" + resource.id + "/process", "POST"));
            resource.links.add(new OrderResource.Link("cancel", "/api/orders/" + resource.id, "DELETE"));
        }
        if ("PAID".equals(order.getPaymentStatus())) {
            resource.links.add(new OrderResource.Link("finish", "/api/orders/" + resource.id + "/finish", "POST"));
            resource.links.add(new OrderResource.Link("invoice", "/api/invoices/" + resource.id + "/pdf?email=user@example.com", "GET"));  // Dodany link do faktury
        }
        return resource;
    }
}