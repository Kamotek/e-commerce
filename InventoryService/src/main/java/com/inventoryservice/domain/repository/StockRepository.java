package com.inventoryservice.domain.repository;
import com.inventoryservice.domain.model.StockItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StockRepository {
    StockItem createItem(StockItem item);
    StockItem updateItem(UUID productId, StockItem item);
    void deleteItem(UUID productId);
    List<StockItem> findAll();
    Optional<StockItem> findByProductId(UUID productId);
    void addStock(UUID productId, int amount);
    void removeStock(UUID productId, int amount);
}