package com.orderservice.domain.model;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private UUID productId;
    private int quantity;
}
