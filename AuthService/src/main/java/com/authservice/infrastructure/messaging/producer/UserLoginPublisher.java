package com.authservice.infrastructure.messaging.producer;

import com.authservice.application.command.model.UserLoggedInEvent;
import com.authservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginPublisher {
    private final RabbitTemplate rabbitTemplate;
    public void publish(UserLoggedInEvent evt) {
        log.info("Publishing user login event");
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_LOGIN_EXCHANGE,
                RabbitMQConfig.USER_LOGIN_ROUTING,
                evt
        );
    }
}
