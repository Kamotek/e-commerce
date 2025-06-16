package com.welcomenotificationservice.application.command.handler;

import com.welcomenotificationservice.application.command.model.CreateWelcomeNotificationCommand;
import com.welcomenotificationservice.domain.model.WelcomeNotification;
import com.welcomenotificationservice.domain.repository.WelcomeNotificationRepository;
import com.welcomenotificationservice.infrastructure.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateWelcomeNotificationCommandHandler {

    private final WelcomeNotificationRepository repo;
    private final EmailService emailService;


    public WelcomeNotification handle(CreateWelcomeNotificationCommand cmd) {
        WelcomeNotification notification = WelcomeNotification.builder()
                .userId(cmd.getUserId())
                .email(cmd.getEmail())
                .title(cmd.getTitle())
                .body(cmd.getBody())
                .build();

        WelcomeNotification saved = repo.create(notification);

        String textBody = saved.getTitle() + "\n\n" + saved.getBody();

        emailService.sendTextEmail(
                saved.getEmail(),
                saved.getTitle(),
                textBody
        );

        return saved;
    }
}
