package com.catalogservice.infrastructure.messaging.producer;

import com.catalogservice.application.command.model.UpdateProductCommand;
import com.catalogservice.infrastructure.configuration.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateProductPublisher {
    private final RabbitTemplate rabbitTemplate;

    public UpdateProductPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(UpdateProductCommand command) {
        log.info("Publishing event {}", command);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.UPDATE_ROUTING_KEY,
                command
        );
    }
}
