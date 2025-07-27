package com.bffservice.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Represents single ordered item
 * Stored as a ListItem inside Order class
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private UUID productId;
    private int quantity;
}
