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
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${jwt.secret}")
    private String jwtSecret;

    private final RegisterUserCommandHandler registerHandler;
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    public AuthController(RegisterUserCommandHandler registerHandler, LoginUserCommandHandler loginHandler, UserRepository userRepository, AuthenticationManager authManager) {
        this.registerHandler = registerHandler;
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
    public ResponseEntity<?> loginUser(@RequestBody LoginUserCommand command, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                command.getEmail(), command.getPassword());
        authManager.authenticate(authToken);

        String jwt = generateJwtToken(command.getEmail(), authToken);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        log.info("User logged in");
        return ResponseEntity.ok(Map.of("message", "Logged in successfully"));
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Get all users");
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/setAdmin/{id}")
    public ResponseEntity<?> setAdmin(@PathVariable UUID id) {
        userRepository.updateUserRole(id, "ADMIN");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        log.info("User logged out");
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private String generateJwtToken(String email, UsernamePasswordAuthenticationToken auth) {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        Date now = new Date();
        Date exp = new Date(now.getTime() + 3_600_000L);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        List<String> roles = List.of(user.getRole());


        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("roles", roles)
                .claim("userId", user.getId())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


}
