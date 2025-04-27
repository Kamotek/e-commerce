package com.notificationservice.infrastructure.messaging.consumer;

import com.notificationservice.application.command.model.CreateNotificationCommand;
import com.notificationservice.application.command.handler.CreateNotificationCommandHandler;
import com.notificationservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {
    @RabbitListener(queues = RabbitMQConfig.CREATE_QUEUE)
    public void onCreateNotification(CreateNotificationCommand cmd) {
        log.info("Received CreateNotificationCommand: {}", cmd);
    }
}
