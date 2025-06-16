package com.orderservice.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Order {
    private UUID id;
    private UUID userId;
    private List<OrderItem> items;
    private Instant orderDate;
    private boolean finished;

    private String shippingStreet;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingCountry;

    private PaymentStatus paymentStatus;
    private BigDecimal totalAmount;

    public enum PaymentStatus {
        PENDING, CLOSED, FAILED, PAID
    }


}
