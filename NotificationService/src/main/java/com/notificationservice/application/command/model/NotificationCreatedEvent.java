package com.notificationservice.application.command.model;


import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NotificationCreatedEvent {
    private UUID notificationId;
    private String userEmail;
    private String title;
    private String body;
    private Instant createdAt;
}
