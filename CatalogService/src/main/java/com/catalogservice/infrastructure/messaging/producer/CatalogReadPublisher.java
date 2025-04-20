package com.catalogservice.infrastructure.messaging.producer;

import com.catalogservice.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class CatalogReadPublisher {
    private final RabbitTemplate rabbitTemplate;

    public CatalogReadPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishReadEvent(UUID productId) {
        Map<String,Object> event = Map.of(
                "productId", productId,
                "timestamp", Instant.now().toString()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                "catalog.read",
                event
        );
    }
}
