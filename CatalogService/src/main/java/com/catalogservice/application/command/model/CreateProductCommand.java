package com.catalogservice.application.command.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductCommand {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String category;
    private Integer inventory;
    private String status;
    private String brand;
    private String badge;
    private Double rating;
    private Integer reviewCount;
    private String specification;
    private String imageUrls; // JSON array as String
}
