package com.inventoryservice.application.command.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddItemCommand {
    private String name;
    private String category;
    private String specification;
    private String description;
    private int quantity;
    private float price;
    private Instant createdAt;
    private Instant updatedAt;
}
