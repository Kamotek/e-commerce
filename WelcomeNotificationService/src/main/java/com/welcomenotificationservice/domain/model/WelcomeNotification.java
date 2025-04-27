package com.welcomenotificationservice.domain.model;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WelcomeNotification {
    private UUID id;
    private UUID userId;
    private String email;
    private String title;
    private String body;
}
