package com.notificationservice.infrastructure.messaging.producer;

import com.notificationservice.application.command.model.CreateNotificationCommand;
import com.notificationservice.application.command.model.NotificationCreatedEvent;
import com.notificationservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {
    private final RabbitTemplate tpl;

    public void publish(NotificationCreatedEvent evt) {
        tpl.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.CREATE_ROUTING,
                evt
        );
    }
}
