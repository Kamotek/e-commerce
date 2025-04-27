package com.notificationservice.application.command.handler;

import com.notificationservice.application.command.model.CreateNotificationCommand;
import com.notificationservice.application.command.model.NotificationCreatedEvent;
import com.notificationservice.domain.model.Notification;
import com.notificationservice.domain.repository.NotificationRepository;
import com.notificationservice.infrastructure.messaging.producer.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CreateNotificationCommandHandler {
    private final NotificationRepository repo;
    private final NotificationPublisher publisher;

    public Notification handle(CreateNotificationCommand cmd) {
        Notification saved = repo.createNotification(
                Notification.builder()
                        .userEmail(cmd.getUserEmail())
                        .title(cmd.getTitle())
                        .body(cmd.getBody())
                        .build()
        );

        NotificationCreatedEvent evt = NotificationCreatedEvent.builder()
                .notificationId(saved.getNotificationId())
                .userEmail(saved.getUserEmail())
                .title(saved.getTitle())
                .body(saved.getBody())
                .createdAt(Instant.now())
                .build();

        publisher.publish(evt);
        return saved;
    }
}
