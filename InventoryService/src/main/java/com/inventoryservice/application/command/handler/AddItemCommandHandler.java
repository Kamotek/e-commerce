package com.inventoryservice.application.command.handler;

import com.inventoryservice.application.command.model.AddItemCommand;
import com.inventoryservice.domain.model.StockItem;
import com.inventoryservice.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddItemCommandHandler {
    private final StockRepository stockRepository;

    public void handle(AddItemCommand command) {
        StockItem item = new StockItem(
                UUID.randomUUID(),
                command.getName(),
                command.getCategory(),
                command.getSpecification(),
                command.getDescription(),
                command.getQuantity(),
                command.getPrice(),
                command.getCreatedAt(),
                command.getUpdatedAt()
        );
        stockRepository.createItem(item);
    }
}
