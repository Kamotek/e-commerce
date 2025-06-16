package com.notificationservice.application.command.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessedEvent {
    private UUID orderId;
    private UUID paymentId;
    private UUID userId;
    private String status;
}