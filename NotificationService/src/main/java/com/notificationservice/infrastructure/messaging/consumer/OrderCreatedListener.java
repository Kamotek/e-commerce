package com.notificationservice.infrastructure.messaging.consumer;

import com.notificationservice.application.command.model.CreateOrderEvent;
import com.notificationservice.domain.model.Notification;
import com.notificationservice.domain.repository.NotificationRepository;
import com.notificationservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void onOrderCreated(CreateOrderEvent event) {
        log.info("Received order created event: {}", event);
        notificationRepository.createNotification(new Notification(
                UUID.randomUUID(),
                "USEREMAIL",
                "ORDER NUMBER " + event.getOrderId(),
                "Potwierdzenie",
                event.getOrderDate()
        ));
    }
}

