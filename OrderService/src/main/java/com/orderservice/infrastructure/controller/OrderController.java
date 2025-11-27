package com.orderservice.infrastructure.controller;

import com.orderservice.application.command.model.CreateOrderCommand;
import com.orderservice.application.command.handler.CreateOrderCommandHandler;
import com.orderservice.domain.model.Order;
import com.orderservice.domain.model.OrderItem;
import com.orderservice.domain.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderCommandHandler createHandler;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<OrderResource> createOrder(@Valid @RequestBody CreateOrderCommand command) {
        Order created = createHandler.handle(command);
        URI location = URI.create("/api/orders/" + created.getId());
        return ResponseEntity.created(location).body(mapOrderToResource(created));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResource> getOrder(@PathVariable UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .map(order -> ResponseEntity.ok(mapOrderToResource(order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderResource>> listOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResource> resources = new ArrayList<>();
        for (Order order : orders) {
            resources.add(mapOrderToResource(order));
        }
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResource> updateOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody Order order) {

        order.setId(orderId);
        Order updated = orderRepository.updateOrder(order);
        return ResponseEntity.ok(mapOrderToResource(updated));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        orderRepository.deleteOrder(orderId.toString());
        return ResponseEntity.noContent().build();
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
        resource.paymentStatus = order.getPaymentStatus().name();
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
        if ("PENDING".equals(resource.paymentStatus)) {
            resource.links.add(new OrderResource.Link("pay", "/api/orders/" + resource.id + "/pay", "POST"));
            resource.links.add(new OrderResource.Link("cancel", "/api/orders/" + resource.id, "DELETE"));
        }
        if ("PAID".equals(resource.paymentStatus)) {
            resource.links.add(new OrderResource.Link("finish", "/api/orders/" + resource.id + "/finish", "POST"));
            resource.links.add(new OrderResource.Link("invoice", "/api/orders/" + resource.id + "/invoice", "GET"));  // Dodany link do faktury
        }
        return resource;
    }


    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderResource> payOrder(@PathVariable UUID orderId) {
        var optionalOrder = orderRepository.findByOrderId(orderId);
        if (optionalOrder.isEmpty()) return ResponseEntity.notFound().build();

        Order order = optionalOrder.get();

        if (!"PENDING".equals(order.getPaymentStatus().name())) {
            return ResponseEntity.badRequest().build();
        }

        order.setPaymentStatus(Order.PaymentStatus.PAID);
        orderRepository.updateOrder(order);

        return ResponseEntity.ok(mapOrderToResource(order));
    }

    @PostMapping("/{orderId}/finish")
    public ResponseEntity<OrderResource> finishOrder(@PathVariable UUID orderId) {
        var optionalOrder = orderRepository.findByOrderId(orderId);
        if (optionalOrder.isEmpty()) return ResponseEntity.notFound().build();

        Order order = optionalOrder.get();

        if (!"PAID".equals(order.getPaymentStatus().name()) || order.isFinished()) {
            return ResponseEntity.badRequest().build();
        }

        order.setFinished(true);
        orderRepository.updateOrder(order);

        return ResponseEntity.ok(mapOrderToResource(order));
    }   

    @GetMapping("/{orderId}/invoice")
    public ResponseEntity<Void> getInvoice(@PathVariable UUID orderId) {
    var optionalOrder = orderRepository.findByOrderId(orderId);
    if (optionalOrder.isEmpty() || !"PAID".equals(optionalOrder.get().getPaymentStatus().name())) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.status(HttpStatus.FOUND)
            .location(URI.create("http://bff-service:8085/api/invoices/" + orderId + "/pdf"))
            .build();
    }
}   