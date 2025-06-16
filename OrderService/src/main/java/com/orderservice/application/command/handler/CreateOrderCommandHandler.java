package com.orderservice.application.command.handler;

import com.orderservice.application.command.model.CreateOrderCommand;
import com.orderservice.application.command.model.CreateOrderEvent;
import com.orderservice.domain.model.Order;
import com.orderservice.domain.model.OrderItem;
import com.orderservice.domain.repository.OrderRepository;
import com.orderservice.infrastructure.client.CatalogServiceClient;
import com.orderservice.application.command.model.ProductDTO;
import com.orderservice.infrastructure.messaging.producer.OrderPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderCommandHandler {

    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;
    private final CatalogServiceClient catalogServiceClient;

    public Order handle(CreateOrderCommand command) {
        List<OrderItem> items = command.getItems().stream()
                .map(i -> OrderItem.builder()
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .build())
                .toList();

        BigDecimal totalAmount = calculateTotalAmount(items);

        Order order = Order.builder()
                .id(UUID.randomUUID())
                .userId(command.getUserId())
                .orderDate(command.getOrderDate() != null ? command.getOrderDate() : Instant.now())
                .items(items)
                .finished(false)
                .shippingStreet(command.getShippingStreet())
                .shippingCity(command.getShippingCity())
                .shippingPostalCode(command.getShippingPostalCode())
                .shippingCountry(command.getShippingCountry())
                .paymentStatus(Order.PaymentStatus.PENDING)
                .totalAmount(totalAmount)
                .build();

        orderRepository.createOrder(order);

        CreateOrderEvent event = CreateOrderEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .totalAmount(totalAmount)
                .shippingStreet(order.getShippingStreet())
                .shippingCity(order.getShippingCity())
                .shippingPostalCode(order.getShippingPostalCode())
                .shippingCountry(order.getShippingCountry())
                .paymentStatus(order.getPaymentStatus().name())
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

    private BigDecimal calculateTotalAmount(List<OrderItem> items) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            try {
                log.info("Fetching price for product ID: {}", item.getProductId());
                ProductDTO product = catalogServiceClient.getProductById(item.getProductId());
                BigDecimal itemPrice = product.getPrice();
                BigDecimal itemTotal = itemPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                total = total.add(itemTotal);
            } catch (Exception e) {
                log.error("Could not fetch price for product {}. Error: {}", item.getProductId(), e.getMessage());
                throw new IllegalStateException("Price for product " + item.getProductId() + " could not be determined.");
            }
        }
        log.info("Calculated total amount: {}", total);
        return total;
    }
}