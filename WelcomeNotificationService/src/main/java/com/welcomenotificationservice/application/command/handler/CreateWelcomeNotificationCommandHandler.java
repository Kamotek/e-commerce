package com.welcomenotificationservice.application.command.handler;

import com.welcomenotificationservice.application.command.model.CreateWelcomeNotificationCommand;
import com.welcomenotificationservice.domain.model.WelcomeNotification;
import com.welcomenotificationservice.domain.repository.WelcomeNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateWelcomeNotificationCommandHandler {
    private final WelcomeNotificationRepository repo;

    public WelcomeNotification handle(CreateWelcomeNotificationCommand cmd) {
        WelcomeNotification n = WelcomeNotification.builder()
                .userId(cmd.getUserId())
                .email(cmd.getEmail())
                .title(cmd.getTitle())
                .body(cmd.getBody())
                .build();

        return repo.create(n);
    }
}
