package com.inventoryservice.infrastructure.messaging.producer;

import com.inventoryservice.application.command.model.AddStockCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.inventoryservice.infrastructure.configuration.RabbitMQConfig.INVENTORY_EXCHANGE;
import static com.inventoryservice.infrastructure.configuration.RabbitMQConfig.ADD_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockAddedPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(AddStockCommand command) {
        log.info("Publishing stock added event");
        rabbitTemplate.convertAndSend(INVENTORY_EXCHANGE, ADD_KEY, command);
    }
}
