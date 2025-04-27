package com.inventoryservice.infrastructure.messaging.producer;

import com.inventoryservice.application.command.model.RemoveStockCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.inventoryservice.infrastructure.configuration.RabbitMQConfig.INVENTORY_EXCHANGE;
import static com.inventoryservice.infrastructure.configuration.RabbitMQConfig.REMOVE_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockRemovedPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(RemoveStockCommand command) {
        log.info("Publishing stock removed message to RabbitMQ");
        rabbitTemplate.convertAndSend(INVENTORY_EXCHANGE, REMOVE_KEY, command);
    }
}
