package com.catalogservice.infrastructure.persistence.repository;

import com.catalogservice.domain.model.Product;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.infrastructure.persistence.entity.ProductEntity;
import com.catalogservice.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
    public void deleteProductById(UUID id) {
        productJpaRepository.deleteById(id);
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

    @Override
    public List<Product> findAllFeatured() {
        return productJpaRepository.findAllFeatured().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }


    @Override
    public Page<Product> findByFilters(
            String category,
            BigDecimal maxPrice,
            String brand,
            boolean availableOnly,
            Pageable pageable
    ) {
        Page<ProductEntity> entityPage = productJpaRepository.findByFilters(
                category,
                maxPrice,
                brand,
                availableOnly,
                pageable
        );

        List<Product> domainList = entityPage.getContent().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());

        return new PageImpl<>(
                domainList,
                pageable,
                entityPage.getTotalElements()
        );
    }


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

}
