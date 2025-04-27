package com.notificationservice.infrastructure.messaging.producer;

import com.notificationservice.application.command.model.CreateNotificationCommand;
import com.notificationservice.application.command.model.NotificationCreatedEvent;
import com.notificationservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationPublisher {
    private final RabbitTemplate tpl;

    public void publish(NotificationCreatedEvent evt) {
        log.info("Publishing notification event: {}", evt);
        tpl.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.CREATE_ROUTING,
                evt
        );
    }
}
