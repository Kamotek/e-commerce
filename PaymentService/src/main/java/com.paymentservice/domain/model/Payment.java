package com.paymentservice.domain.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private UUID paymentId;
    private UUID userId;
    private UUID orderId;
    private BigDecimal amount;
    private Instant createdAt;
}