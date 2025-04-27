package com.authservice.infrastructure.controller;

import com.authservice.application.command.handler.LoginUserCommandHandler;
import com.authservice.application.command.handler.RegisterUserCommandHandler;
import com.authservice.application.command.model.LoginUserCommand;
import com.authservice.application.command.model.RegisterUserCommand;
import com.authservice.domain.model.User;
import com.authservice.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserCommandHandler registerHandler;
    private final LoginUserCommandHandler loginHandler;
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    public AuthController(RegisterUserCommandHandler registerHandler, LoginUserCommandHandler loginHandler, UserRepository userRepository, AuthenticationManager authManager) {
        this.registerHandler = registerHandler;
        this.loginHandler = loginHandler;
        this.userRepository = userRepository;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserCommand command) {
        registerHandler.handle(command);
        log.info("User registered");
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserCommand command) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                command.getEmail(), command.getPassword());
        authManager.authenticate(authToken);

        UUID userId = loginHandler.handle(command);

        String jwt = generateJwtToken(authToken);

        Map<String,Object> body = Map.of(
                "access_token", jwt,
                "token_type", "Bearer",
                "expires_in", 3600,
                "userId", userId
        );
        log.info("User logged in");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Get all users");
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }


    private String generateJwtToken(UsernamePasswordAuthenticationToken auth) {
        // TODO
        log.info("Generate JWT token");
        return "...";
    }
}
