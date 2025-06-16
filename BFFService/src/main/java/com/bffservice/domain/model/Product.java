package com.bffservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String category;
    private Integer inventory;
    private String status;
    private List<String> imageUrls;
    private Double rating;
    private Integer reviewCount;
    private Map<String, String> specifications;
    private String brand;
    private String badge;
}
