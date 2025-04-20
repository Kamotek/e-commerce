package com.inventoryservice.application.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor
public class CatalogReadEvent {
    private UUID productId;
    private int amount;
}
