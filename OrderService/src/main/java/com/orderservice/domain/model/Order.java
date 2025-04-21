package com.orderservice.domain.model;

import lombok.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
}
