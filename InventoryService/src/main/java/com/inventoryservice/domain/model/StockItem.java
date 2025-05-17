package com.inventoryservice.domain.model;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class StockItem {
    private UUID productId;
    private String name;
    private String category;
    private String specification;
    private String description;
    private int quantity;
    private float price;
    private Instant createdAt;
    private Instant updatedAt;
}
