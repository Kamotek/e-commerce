package com.inventoryservice.infrastructure.persistence.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;
import com.inventoryservice.infrastructure.persistence.entity.StockItemEntity;

@Repository
public interface StockItemJpaRepository extends JpaRepository<StockItemEntity, UUID> {
    Optional<StockItemEntity> findByProductId(UUID productId);
}