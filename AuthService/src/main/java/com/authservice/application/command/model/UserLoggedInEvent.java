package com.authservice.application.command.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserLoggedInEvent {
    private UUID userId;
    private Instant loginAt;
}
