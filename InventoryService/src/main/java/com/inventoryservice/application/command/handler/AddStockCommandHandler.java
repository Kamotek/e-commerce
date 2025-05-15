package com.inventoryservice.application.command.handler;

import com.inventoryservice.application.command.model.AddStockCommand;
import com.inventoryservice.domain.repository.StockRepository;
import com.inventoryservice.infrastructure.messaging.producer.StockAddedPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddStockCommandHandler {
    private final StockRepository repository;
    private final StockAddedPublisher publisher;

    public void handle(AddStockCommand command) {
        repository.addStock(command.getProductId(), command.getAmount());
        publisher.publish(command);
    }
}
