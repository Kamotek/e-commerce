package com.inventoryservice.infrastructure.messaging.producer;

import com.inventoryservice.application.command.model.RemoveStockCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.inventoryservice.infrastructure.configuration.RabbitMQConfig.EXCHANGE;
import static com.inventoryservice.infrastructure.configuration.RabbitMQConfig.REMOVE_KEY;

@Service
@RequiredArgsConstructor
public class StockRemovedPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(RemoveStockCommand command) {
        rabbitTemplate.convertAndSend(EXCHANGE, REMOVE_KEY, command);
    }
}
