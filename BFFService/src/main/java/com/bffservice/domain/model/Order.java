package com.bffservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Represents an order within our e-commerce system.
 * This class is a domain model that holds all information related to a single customer order,
 * including the items purchased, shipping details, and payment status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private String paymentStatus;
    private BigDecimal totalAmount;
}