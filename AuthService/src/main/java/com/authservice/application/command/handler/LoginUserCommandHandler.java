package com.authservice.application.command.handler;

import com.authservice.application.command.model.LoginUserCommand;
import com.authservice.application.command.model.UserLoggedInEvent;
import com.authservice.domain.model.User;
import com.authservice.domain.repository.UserRepository;
import com.authservice.infrastructure.messaging.producer.UserLoginPublisher;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginUserCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginUserCommandHandler.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserLoginPublisher loginPublisher;

    public UUID handle(LoginUserCommand command) {
        logger.info("Logowanie użytkownika {}", command.getEmail());

        Optional<User> userOpt = userRepository.findByEmail(command.getEmail());
        if (userOpt.isEmpty()) return null;

        User user = userOpt.get();
        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            logger.warn("Błędne hasło dla {}", user.getEmail());
            return null;
        }

        // budujemy event i publikujemy
        UserLoggedInEvent evt = UserLoggedInEvent.builder()
                .userId(user.getId())
                .loginAt(Instant.now())
                .build();

        loginPublisher.publish(evt);
        logger.info("Opublikowano event UserLoggedInEvent dla userId={}", user.getId());

        return user.getId();
    }
}
