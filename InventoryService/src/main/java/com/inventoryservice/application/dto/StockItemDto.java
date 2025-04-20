package com.inventoryservice.application.dto;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class StockItemDto {
    private UUID productId;
    private String name;
    private String category;
    private String description;
    private int quantity;
    private Instant createdAt;
    private Instant updatedAt;
}
