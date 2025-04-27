package com.inventoryservice.infrastructure.messaging.producer;

import com.inventoryservice.application.command.model.AddStockCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.inventoryservice.infrastructure.configuration.RabbitMQConfig.INVENTORY_EXCHANGE;
import static com.inventoryservice.infrastructure.configuration.RabbitMQConfig.ADD_KEY;

@Service
@RequiredArgsConstructor
public class StockAddedPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(AddStockCommand command) {
        rabbitTemplate.convertAndSend(INVENTORY_EXCHANGE, ADD_KEY, command);
    }
}
