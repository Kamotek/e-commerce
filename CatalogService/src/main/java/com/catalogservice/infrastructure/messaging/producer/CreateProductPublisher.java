package com.catalogservice.infrastructure.messaging.producer;

import com.catalogservice.application.command.model.CreateProductCommand;
import com.catalogservice.infrastructure.configuration.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CreateProductPublisher {
    private final RabbitTemplate rabbitTemplate;

    public CreateProductPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(CreateProductCommand command) {
        log.info("Publishing event {}", command);
        rabbitTemplate.convertAndSend(
        RabbitMQConfig.EXCHANGE_NAME,
        RabbitMQConfig.CREATE_ROUTING_KEY,
        command
        );
    }
}
