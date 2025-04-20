package com.catalogservice.infrastructure.messaging.producer;

import com.catalogservice.application.command.model.UpdateProductCommand;
import com.catalogservice.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UpdateProductPublisher {
    private final RabbitTemplate rabbitTemplate;

    public UpdateProductPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(UpdateProductCommand command) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.UPDATE_ROUTING_KEY,
                command
        );
    }
}
