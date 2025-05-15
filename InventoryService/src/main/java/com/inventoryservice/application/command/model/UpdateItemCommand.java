package com.inventoryservice.application.command.model;

import java.time.Instant;
import java.util.UUID;

public class UpdateItemCommand {
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
