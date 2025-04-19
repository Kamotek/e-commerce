package com.authservice.infrastructure.messaging.producer;

import com.authservice.application.command.model.LoginUserCommand;
import com.authservice.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserLoginPublisher {
    private final RabbitTemplate rabbitTemplate;

    public UserLoginPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(LoginUserCommand command) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_LOGIN_EXCHANGE,
                RabbitMQConfig.USER_LOGIN_ROUTING,
                command
        );
    }
}
