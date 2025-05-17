package com.catalogservice.infrastructure.persistence.repository;

import com.catalogservice.domain.model.Product;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.infrastructure.persistence.entity.ProductEntity;import com.catalogservice.util.JsonUtils;
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
        // Ensure constructor parameters follow ProductEntity field order
        ProductEntity entity = new ProductEntity(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getOriginalPrice(),
                product.getCategory(),
                product.getInventory(),
                product.getStatus(),
                product.getBrand(),
                product.getBadge(),
                product.getRating(),
                product.getReviewCount(),
                JsonUtils.mapToJson(product.getSpecifications()),
                JsonUtils.listToJson(product.getImageUrls())
        );
        productJpaRepository.save(entity);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return productJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return productJpaRepository.findByName(name)
                .map(this::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Map JPA entity to domain model
     */
    private Product toDomain(ProductEntity e) {
        return Product.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .price(e.getPrice())
                .originalPrice(e.getOriginalPrice())
                .category(e.getCategory())
                .inventory(e.getInventory())
                .status(e.getStatus())
                .brand(e.getBrand())
                .badge(e.getBadge())
                .rating(e.getRating())
                .reviewCount(e.getReviewCount())
                .specifications(JsonUtils.jsonToMap(e.getSpecification()))
                .imageUrls(JsonUtils.jsonToList(e.getImageUrls()))
                .build();
    }

    public List<Product> findAllFeatured() {
        return productJpaRepository.findAllFeatured().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}
