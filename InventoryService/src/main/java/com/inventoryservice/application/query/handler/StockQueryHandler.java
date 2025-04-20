package com.inventoryservice.application.query.handler;

import com.inventoryservice.application.query.model.StockLevelQuery;
import com.inventoryservice.domain.model.StockItem;
import com.inventoryservice.domain.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockQueryHandler {
    private final StockRepository repo;
    public StockQueryHandler(StockRepository repo) { this.repo = repo; }
    public StockItem handle(StockLevelQuery q) { return repo.findByProductId(q.getProductId()).orElse(null); }
}