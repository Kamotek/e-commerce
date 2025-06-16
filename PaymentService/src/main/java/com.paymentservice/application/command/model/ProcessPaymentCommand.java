package com.paymentservice.application.command.model;

import java.util.UUID;

public record ProcessPaymentCommand(UUID orderId, String cardNumber) {}