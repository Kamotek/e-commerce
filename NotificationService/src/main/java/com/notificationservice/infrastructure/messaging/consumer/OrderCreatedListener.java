package com.notificationservice.infrastructure.messaging.consumer;

import com.notificationservice.application.command.model.CreateOrderEvent;
import com.notificationservice.domain.model.Notification;
import com.notificationservice.domain.repository.NotificationRepository;
import com.notificationservice.infrastructure.client.AuthServiceClient;
import com.notificationservice.infrastructure.mail.EmailService;
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
    private final EmailService emailService;
    private final AuthServiceClient authClient;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void onOrderCreated(CreateOrderEvent event) {
        log.info("Received order created event: {}", event);

        AuthServiceClient.UserDto user = authClient.getUser(event.getUserId());
        String email = user.email();

        Notification saved = notificationRepository.createNotification(Notification.builder()
                .notificationId(UUID.randomUUID())
                .userEmail(email)
                .title("Potwierdzenie zamówienia")
                .body("Twoje zamówienie nr " + event.getOrderId() + " zostało przyjęte.")
                .createdAt(event.getOrderDate())
                .build()
        );

        String subject = saved.getTitle();
        String body    = String.format(
                "Cześć,\n\nDziękujemy za zamówienie o numerze %s.\nData: %s\n\nPozdrawiamy!",
                event.getOrderId(), event.getOrderDate()
        );
        emailService.sendTextEmail(email, subject, body);
    }
}
