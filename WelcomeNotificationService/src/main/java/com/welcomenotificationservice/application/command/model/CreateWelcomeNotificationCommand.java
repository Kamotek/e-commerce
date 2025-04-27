package com.welcomenotificationservice.application.command.model;

import lombok.*;

import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateWelcomeNotificationCommand {
    private UUID userId;
    private String email;
    private String title;
    private String body;
}
