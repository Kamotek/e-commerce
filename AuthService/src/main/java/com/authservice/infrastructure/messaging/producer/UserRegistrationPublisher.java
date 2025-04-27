package com.authservice.infrastructure.messaging.producer;

import com.authservice.application.command.model.UserRegisteredEvent;
import com.authservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(UserRegisteredEvent evt) {
        log.info("Publishing user registration event: {}", evt);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_REG_EXCHANGE,
                RabbitMQConfig.USER_REG_ROUTING,
                evt
        );
    }
}
