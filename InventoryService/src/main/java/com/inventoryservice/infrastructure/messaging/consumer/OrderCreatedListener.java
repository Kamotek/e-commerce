package com.inventoryservice.infrastructure.messaging.consumer;


import com.inventoryservice.application.event.CreateOrderEvent;
import com.inventoryservice.domain.repository.StockRepository;
import com.inventoryservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {
    private final StockRepository stockRepository;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void onOrderCreated(CreateOrderEvent event) {
        event.getItems().forEach(item -> {
            try {
                stockRepository.removeStock(item.getProductId(), item.getQuantity());
                log.info("Removed {} units for product {}", item.getQuantity(), item.getProductId());
            } catch (Exception ex) {
                log.warn("Failed to remove stock for {}: {}", item.getProductId(), ex.getMessage());
            }
        });
    }
}