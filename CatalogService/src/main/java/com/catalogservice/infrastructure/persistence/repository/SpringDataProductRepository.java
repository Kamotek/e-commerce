package com.catalogservice.infrastructure.persistence.repository;

import com.catalogservice.domain.model.Product;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.infrastructure.persistence.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class SpringDataProductRepository implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Autowired
    public SpringDataProductRepository(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public void save(Product product) {
        ProductEntity entity = new ProductEntity(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory()
                );
        productJpaRepository.save(entity);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return productJpaRepository.findById(id)
                .map(entity -> new Product(
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getPrice(),
                        entity.getCategory()
                ));
    }

    @Override
    public Optional<Product> findByName(String name) {
        return productJpaRepository.findByName(name)
                .map(entity -> new Product(
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getPrice(),
                        entity.getCategory()
                ));
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll().stream().map(
                entity -> new Product(
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getPrice(),
                        entity.getCategory()))
                .collect(Collectors.toList());
    }
}
