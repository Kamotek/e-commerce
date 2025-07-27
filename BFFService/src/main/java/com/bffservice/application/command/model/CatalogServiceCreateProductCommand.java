package com.bffservice.application.command.model;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;



/**
 * Represents a command to create a new product in the Catalog Service.
 * This class is used for communication with the Catalog Service via a Feign client.
 * Note that some fields like {@code imageUrls} and {@code specifications} are expected to be JSON strings
 * for serialization purposes.
 */
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CatalogServiceCreateProductCommand {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String category;
    private Integer inventory;
    private String status;
    private String imageUrls;
    private Double rating;
    private Integer reviewCount;
    private String specifications;
    private String brand;
    private String badge;
}
