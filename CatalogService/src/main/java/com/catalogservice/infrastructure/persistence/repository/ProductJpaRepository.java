package com.catalogservice.infrastructure.persistence.repository;

import com.catalogservice.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findByName(String name);

    @Query("SELECT p FROM ProductEntity p " +
            "WHERE (:category IS NULL OR p.category = :category) " +
            "  AND (:brand IS NULL OR p.brand = :brand) " +
            "  AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "  AND (:availableOnly = false OR p.inventory > 0)")
    Page<ProductEntity> findByFilters(
            @Param("category") String category,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brand") String brand,
            @Param("availableOnly") boolean availableOnly,
            Pageable pageable
    );

    @Query("SELECT p FROM ProductEntity p WHERE p.badge IS NOT NULL")
    List<ProductEntity> findAllFeatured();
}
