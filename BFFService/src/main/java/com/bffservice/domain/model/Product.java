package com.bffservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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