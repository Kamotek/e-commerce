package com.authservice.application.command.handler;

import com.authservice.application.command.model.RegisterUserCommand;
import com.authservice.domain.model.User;
import com.authservice.domain.repository.UserRepository;
import com.authservice.infrastructure.messaging.producer.UserRegistrationPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegisterUserCommandHandler.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRegistrationPublisher publisher;

    public RegisterUserCommandHandler(PasswordEncoder passwordEncoder, UserRepository userRepository, UserRegistrationPublisher publisher) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.publisher = publisher;
    }

    public void handle(RegisterUserCommand command) {
        logger.info("Rejestracja użytkownika: {}", command.getEmail());

        String hashedPassword = passwordEncoder.encode(command.getPassword());
        User user = new User(command.getEmail(), hashedPassword, command.getFirstName(), command.getLastName());
        userRepository.save(user);

        publisher.publish(command);

        logger.info("Użytkownik zarejestrowany: {}", user.getEmail());
    }
}

