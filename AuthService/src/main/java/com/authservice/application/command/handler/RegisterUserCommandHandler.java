package com.authservice.application.command.handler;

import com.authservice.application.command.model.RegisterUserCommand;
import com.authservice.application.command.model.UserRegisteredEvent;
import com.authservice.domain.model.User;
import com.authservice.domain.repository.UserRepository;
import com.authservice.infrastructure.messaging.producer.UserRegistrationPublisher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegisterUserCommandHandler.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRegistrationPublisher publisher;

    public void handle(RegisterUserCommand command) {
        logger.info("Rejestracja użytkownika: {}", command.getEmail());

        String hashedPassword = passwordEncoder.encode(command.getPassword());
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(command.getEmail())
                .password(hashedPassword)
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .createdAt(Instant.now())
                .role("USER")
                .build();
        userRepository.save(user);

        UserRegisteredEvent evt = UserRegisteredEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .build();

        publisher.publish(evt);
        logger.info("Opublikowano event UserRegisteredEvent dla userId={}", user.getId());
    }
}
