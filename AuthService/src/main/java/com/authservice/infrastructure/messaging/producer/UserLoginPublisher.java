package com.authservice.infrastructure.messaging.producer;

import com.authservice.application.command.model.UserLoggedInEvent;
import com.authservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(UserLoggedInEvent evt) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_LOGIN_EXCHANGE,
                RabbitMQConfig.USER_LOGIN_ROUTING,
                evt
        );
    }
}
