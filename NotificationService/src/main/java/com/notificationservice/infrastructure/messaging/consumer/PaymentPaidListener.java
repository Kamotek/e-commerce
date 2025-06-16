package com.notificationservice.infrastructure.messaging.consumer;

import com.notificationservice.application.command.model.PaymentProcessedEvent;
import com.notificationservice.domain.model.Notification;
import com.notificationservice.domain.repository.NotificationRepository;
import com.notificationservice.infrastructure.client.AuthServiceClient;
import com.notificationservice.infrastructure.mail.EmailService;
import com.notificationservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentPaidListener {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final AuthServiceClient authClient;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_PAID_QUEUE)
    public void onPaymentProcessed(PaymentProcessedEvent event) {
        if (!"COMPLETED".equals(event.getStatus())) {
            log.info("Skipping notification for payment event with status: {}", event.getStatus());
            return;
        }

        log.info("Received successful payment event for orderId: {}", event.getOrderId());

        try {
            AuthServiceClient.UserDto user = authClient.getUser(event.getUserId());
            String email = user.email();

            String title = "Płatność za Twoje zamówienie została potwierdzona";
            String body = String.format(
                    "Cześć,\n\nTwoja płatność za zamówienie o numerze %s została pomyślnie przetworzona.\n\nDziękujemy za zakupy w ComputeroShop!\n\nPozdrawiamy!",
                    event.getOrderId()
            );

            notificationRepository.createNotification(Notification.builder()
                    .userEmail(email)
                    .title(title)
                    .body(body)
                    .createdAt(Instant.now())
                    .build()
            );

            emailService.sendTextEmail(email, title, body);
            log.info("Successfully sent 'Order Paid' notification to {}", email);

        } catch (Exception e) {
            log.error("Failed to process paid order notification for orderId: {}. Error: {}", event.getOrderId(), e.getMessage());
        }
    }
}