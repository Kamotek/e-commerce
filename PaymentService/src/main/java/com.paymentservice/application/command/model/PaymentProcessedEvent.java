package com.paymentservice.application.command.model;

import java.util.UUID;

public record PaymentProcessedEvent(UUID orderId, UUID paymentId, UUID userId, String status) {}