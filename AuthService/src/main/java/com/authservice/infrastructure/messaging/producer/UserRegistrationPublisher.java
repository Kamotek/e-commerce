package com.authservice.infrastructure.messaging.producer;

import com.authservice.application.command.model.UserRegisteredEvent;
import com.authservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(UserRegisteredEvent evt) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_REG_EXCHANGE,
                RabbitMQConfig.USER_REG_ROUTING,
                evt
        );
    }
}
