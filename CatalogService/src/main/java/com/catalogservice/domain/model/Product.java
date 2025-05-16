package com.catalogservice.domain.model;

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
    private BigDecimal originalPrice; // Do wyświetlania przecen
    private String category;
    private Integer inventory; // Ilość dostępna
    private String status; // "In Stock", "Low Stock", "Out of Stock"
    private List<String> imageUrls; // Lista URL-i do zdjęć
    private Double rating; // Średnia ocena
    private Integer reviewCount; // Liczba recenzji
    private Map<String, String> specifications; // Specyfikacje techniczne jako pary klucz-wartość
    private String brand;
    private String badge; // "New", "Sale", "Popular" itp.
}