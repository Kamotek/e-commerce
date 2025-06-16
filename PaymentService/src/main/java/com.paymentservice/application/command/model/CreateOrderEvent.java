package com.paymentservice.application.command.model;


import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderEvent {
    private UUID orderId;
    private UUID userId;
    private Instant orderDate;
    private BigDecimal totalAmount; 
    private List<OrderItem> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItem {
        private UUID productId;
        private int quantity;
    }
}
