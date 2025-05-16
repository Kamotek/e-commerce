package com.catalogservice.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="products")
public class ProductEntity {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private BigDecimal price;
    @Column
    private BigDecimal originalPrice;
    @Column
    private String category;
    @Column
    private Integer inventory;
    @Column
    private String status;
    @Column
    private String brand;
    @Column
    private String badge;
    @Column
    private Double rating;
    @Column
    private Integer reviewCount;

    @Column(length = 5000)
    private String specification; // JSON string

    @Column(length = 2000)
    private String imageUrls; // JSON array as String

}
