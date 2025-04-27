package com.paymentservice.application.command.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentCommand {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID orderId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Instant createdAt;
}