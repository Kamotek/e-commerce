package com.bffservice.application.command.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProductCommand {
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