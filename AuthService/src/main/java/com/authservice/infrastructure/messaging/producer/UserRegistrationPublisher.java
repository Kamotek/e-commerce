package com.authservice.infrastructure.messaging.producer;

import com.authservice.application.command.model.RegisterUserCommand;
import com.authservice.infrastructure.configuration.RabbitMQConfig;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class UserRegistrationPublisher {
    private final RabbitTemplate rabbitTemplate;

    public UserRegistrationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(RegisterUserCommand command){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_REG_EXCHANGE,
                RabbitMQConfig.USER_REG_ROUTING,
                command
        );
    }
}
