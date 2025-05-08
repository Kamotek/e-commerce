package com.authservice.infrastructure.controller;

import com.authservice.application.command.handler.LoginUserCommandHandler;
import com.authservice.application.command.handler.RegisterUserCommandHandler;
import com.authservice.application.command.model.LoginUserCommand;
import com.authservice.application.command.model.RegisterUserCommand;
import com.authservice.domain.model.User;
import com.authservice.domain.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${jwt.secret}")
    private String jwtSecret;

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

    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Get all users");
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }




    private String generateJwtToken(UsernamePasswordAuthenticationToken auth) {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        Date now = new Date();
        Date exp = new Date(now.getTime() + 3_600_000L); // +1h

        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("roles", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
