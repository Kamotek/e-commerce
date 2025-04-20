package com.inventoryservice.infrastructure.persistence.repository;

import com.inventoryservice.domain.model.StockItem;
import com.inventoryservice.domain.repository.StockRepository;
import com.inventoryservice.infrastructure.persistence.entity.StockItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SpringDataStockRepository implements StockRepository {
    private final StockItemJpaRepository jpa;

    @Override
    public StockItem createItem(StockItem item) {
        StockItemEntity entity = StockItemEntity.builder()
                .productId(UUID.randomUUID())
                .name(item.getName())
                .category(item.getCategory())
                .description(item.getDescription())
                .quantity(item.getQuantity())
                .build();
        StockItemEntity saved = jpa.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public StockItem updateItem(UUID productId, StockItem item) {
        StockItemEntity entity = jpa.findByProductId(productId)
                .orElseThrow(() -> new IllegalStateException("No item found for product " + productId));
        entity.setName(item.getName());
        entity.setCategory(item.getCategory());
        entity.setDescription(item.getDescription());
        entity.setQuantity(item.getQuantity());
        StockItemEntity saved = jpa.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public void deleteItem(UUID productId) {
        jpa.deleteById(productId);
    }

    @Override
    public List<StockItem> findAll() {
        return jpa.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StockItem> findByProductId(UUID productId) {
        return jpa.findByProductId(productId)
                .map(this::mapToDomain);
    }

    @Override
    public void addStock(UUID productId, int amount) {
        StockItemEntity e = jpa.findByProductId(productId)
                .map(ent -> {
                    ent.setQuantity(ent.getQuantity() + amount);
                    return ent;
                })
                .orElse(StockItemEntity.builder()
                        .productId(productId)
                        .quantity(amount)
                        .build());
        jpa.save(e);
    }

    @Override
    public void removeStock(UUID productId, int amount) {
        StockItemEntity entity = jpa.findByProductId(productId)
                .orElseThrow(() -> new IllegalStateException("No stock entry for product " + productId));
        if (entity.getQuantity() < amount) {
            throw new IllegalStateException(
                    String.format("Insufficient stock for product %s: available=%d, requested=%d",
                            productId, entity.getQuantity(), amount)
            );
        }
        entity.setQuantity(entity.getQuantity() - amount);
        jpa.save(entity);
    }

    private StockItem mapToDomain(StockItemEntity e) {
        return StockItem.builder()
                .productId(e.getProductId())
                .name(e.getName())
                .category(e.getCategory())
                .description(e.getDescription())
                .quantity(e.getQuantity())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
