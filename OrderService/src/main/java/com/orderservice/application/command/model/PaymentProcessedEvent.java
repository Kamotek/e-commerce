package com.orderservice.application.command.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentProcessedEvent {
    private UUID orderId;
    private UUID paymentId;
    private String status;
}