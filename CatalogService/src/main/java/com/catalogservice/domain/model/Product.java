package com.catalogservice.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;

    public Product(String name, String description, BigDecimal price, String category) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }
}
