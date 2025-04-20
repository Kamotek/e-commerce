package com.catalogservice.infrastructure.messaging.producer;

import com.catalogservice.application.command.model.CreateProductCommand;
import com.catalogservice.domain.model.Product;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class CreateProductPublisher {
    private final RabbitTemplate rabbitTemplate;

    public CreateProductPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(CreateProductCommand command) {
        rabbitTemplate.convertAndSend(
        RabbitMQConfig.EXCHANGE_NAME,
        RabbitMQConfig.CREATE_ROUTING_KEY,
        command
        );
    }
}
