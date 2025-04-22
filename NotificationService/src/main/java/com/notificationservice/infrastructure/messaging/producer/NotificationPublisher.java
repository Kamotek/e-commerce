package com.notificationservice.infrastructure.messaging.producer;

import com.notificationservice.application.command.model.CreateNotificationCommand;
import com.notificationservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publish(CreateNotificationCommand cmd) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.CREATE_ROUTING,
                cmd
        );
    }
}
