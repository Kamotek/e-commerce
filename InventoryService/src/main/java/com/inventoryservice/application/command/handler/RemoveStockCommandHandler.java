package com.inventoryservice.application.command.handler;

import com.inventoryservice.application.command.model.RemoveStockCommand;
import com.inventoryservice.domain.repository.StockRepository;
import com.inventoryservice.infrastructure.messaging.producer.StockRemovedPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveStockCommandHandler {
    private final StockRepository repository;
    private final StockRemovedPublisher publisher;

    public void handle(RemoveStockCommand command) {
        repository.removeStock(command.getProductId(), command.getAmount());
        publisher.publish(command); // Publish event
    }
}
