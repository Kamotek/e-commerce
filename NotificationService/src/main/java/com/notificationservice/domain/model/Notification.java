package com.notificationservice.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Notification {
    private UUID notificationId;
    private String userEmail;
    private String title;
    private String body;
    private Instant createdAt;
}
