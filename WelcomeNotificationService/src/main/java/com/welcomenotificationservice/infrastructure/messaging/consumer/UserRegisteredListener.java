package com.welcomenotificationservice.infrastructure.messaging.consumer;

import com.welcomenotificationservice.application.command.handler.CreateWelcomeNotificationCommandHandler;
import com.welcomenotificationservice.application.command.model.CreateWelcomeNotificationCommand;
import com.welcomenotificationservice.application.command.model.UserRegisteredEvent;
import com.welcomenotificationservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredListener {
    private final CreateWelcomeNotificationCommandHandler handler;

    @RabbitListener(queues = RabbitMQConfig.WELCOME_QUEUE)
    public void onUserRegistered(UserRegisteredEvent evt) {
        log.info("Otrzymano UserRegisteredEvent dla userId={}", evt.getUserId());

        CreateWelcomeNotificationCommand cmd = CreateWelcomeNotificationCommand.builder()
                .userId(evt.getUserId())
                .email(evt.getEmail())
                .title("Welcome, " + evt.getFirstName() + "!")
                .body("Thanks for registering on " + evt.getCreatedAt())
                .build();

        handler.handle(cmd);
        log.info("WelcomeNotification utworzony dla userId={}", evt.getUserId());
    }
}
