package com.catalogservice.domain.repository;

import com.catalogservice.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    void save(Product product);
    List<Product> findAll();
    Optional<Product> findById(UUID id);
    Optional<Product> findByName(String name);
}
