package com.catalogservice.domain.repository;

import com.catalogservice.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // NOWY: Import dla Pageable

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    void save(Product product);
    void deleteProductById(UUID id);
    List<Product> findAll();
    Optional<Product> findById(UUID id);
    Optional<Product> findByName(String name);
    List<Product> findAllFeatured();


    Page<Product> findByFilters(
            String category,
            BigDecimal maxPrice,
            String brand,
            boolean availableOnly,
            Pageable pageable
    );
}