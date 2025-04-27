package com.paymentservice.application.command.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePaymentCommand {
    @NotNull
    private UUID paymentId;
    @NotNull
    private BigDecimal amount;
}