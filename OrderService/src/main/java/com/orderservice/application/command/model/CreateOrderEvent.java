package com.orderservice.application.command.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateOrderEvent {
    private UUID orderId;
    private UUID userId;
    private Instant orderDate;
    private BigDecimal totalAmount;
    private String shippingStreet;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingCountry;


    private String paymentStatus;
    private List<OrderItem> items;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OrderItem {
        private UUID productId;
        private int quantity;
    }
}