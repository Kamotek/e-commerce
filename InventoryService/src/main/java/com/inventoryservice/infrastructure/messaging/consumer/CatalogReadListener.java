package com.inventoryservice.infrastructure.messaging.consumer;

import com.inventoryservice.application.event.CatalogReadEvent;
import com.inventoryservice.domain.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CatalogReadListener {
    private final StockRepository stockRepository;

    @RabbitListener(queues = "catalog.read.queue")
    public void onRead(CatalogReadEvent event) {
        UUID productId = event.getProductId();
        int amount  = event.getAmount();

        try {
            stockRepository.removeStock(productId, amount);
            log.info("Stock updated for product {} (-{})", productId, amount);
        } catch (DataAccessException | IllegalStateException ex) {
            log.warn("Could not remove stock for {}: {}", productId, ex.getMessage());
        }
    }
}

