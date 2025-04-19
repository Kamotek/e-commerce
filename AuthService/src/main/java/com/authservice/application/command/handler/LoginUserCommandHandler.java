package com.authservice.application.command.handler;

import com.authservice.application.command.model.LoginUserCommand;
import com.authservice.domain.model.User;
import com.authservice.domain.repository.UserRepository;
import com.authservice.infrastructure.messaging.producer.UserLoginPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LoginUserCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginUserCommandHandler.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserLoginPublisher loginPublisher;

    public LoginUserCommandHandler(PasswordEncoder passwordEncoder, UserRepository userRepository, UserLoginPublisher loginPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.loginPublisher = loginPublisher;
    }

    public UUID handle(LoginUserCommand command) {
        logger.info("Logowanie użytkownika {}", command.getEmail());

        Optional<User> user = userRepository.findByEmail(command.getEmail());

        if(user.isPresent()) {
            if(passwordEncoder.matches(command.getPassword(), user.get().getPassword())) {
                loginPublisher.publish(command);
                logger.info("Logowanie pomyślne {}", user.get().getEmail());
                return user.get().getId();
            } else {
                logger.info("Błędne hasło {}", user.get().getEmail());
                return null;
            }
        }
        return null;
    }
}
