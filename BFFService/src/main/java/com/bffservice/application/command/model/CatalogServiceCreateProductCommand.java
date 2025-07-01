package com.bffservice.application.command.model;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
