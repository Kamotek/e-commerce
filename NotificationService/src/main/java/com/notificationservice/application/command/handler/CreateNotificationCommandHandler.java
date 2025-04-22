package com.notificationservice.application.command.handler;

import com.notificationservice.application.command.model.CreateNotificationCommand;
import com.notificationservice.domain.model.Notification;
import com.notificationservice.domain.repository.NotificationRepository;
import com.notificationservice.infrastructure.messaging.producer.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNotificationCommandHandler {
    private final NotificationRepository repo;
    private final NotificationPublisher publisher;

    public Notification handle(CreateNotificationCommand cmd) {
        Notification notif = Notification.builder()
                .userEmail(cmd.getUserEmail())
                .title(cmd.getTitle())
                .body(cmd.getBody())
                .build();

        Notification saved = repo.createNotification(notif);

        publisher.publish(cmd);

        return saved;
    }
}
