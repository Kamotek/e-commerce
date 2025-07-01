package com.bffservice.interfaces.rest.controller;


import com.bffservice.application.command.model.LoginUserCommand;
import com.bffservice.application.command.model.RegisterUserCommand;
import com.bffservice.domain.model.AggregatedUser;
import com.bffservice.interfaces.rest.AuthServiceClient;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthServiceClient authClient;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterUserCommand cmd
    ) {
        try {
            return authClient.register(cmd);
        } catch (FeignException e) {
            log.error("Feign call to register failed: status={}, body={}", e.status(), e.contentUTF8(), e);
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginUserCommand cmd,
            HttpServletResponse servletResponse
    ) {
        try {
            ResponseEntity<Map<String, Object>> resp = authClient.login(cmd);

            List<String> setCookie = resp.getHeaders().get(HttpHeaders.SET_COOKIE);
            if (setCookie != null) {
                setCookie.forEach(cookie -> servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie));
            }

            return ResponseEntity
                    .status(resp.getStatusCode())
                    .body(resp.getBody());
        } catch (FeignException e) {
            log.error("Feign call to login failed: status={}, body={}", e.status(), e.contentUTF8(), e);
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<AggregatedUser>> getAllUsers() {
        try {
            return authClient.getAllUsers();
        } catch (FeignException e) {
            log.error("Feign call to getAllUsers failed: status={}", e.status(), e);
            return ResponseEntity.status(e.status() > 0 ? e.status() : 503).build();
        }
    }

    @PutMapping("/users/{id}/set-admin")
    public ResponseEntity<Void> setAdminRole(@PathVariable("id") String id) {
        try {
            return authClient.setAdmin(id);
        } catch(FeignException e) {
            log.error("Feign call to setAdmin for id {} failed: status={}", id, e.status(), e);

            int status = e.status() > 0 ? e.status() : HttpStatus.SERVICE_UNAVAILABLE.value();
            return ResponseEntity.status(status).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse servletResponse) {
        try {
            ResponseEntity<Map<String, Object>> resp = authClient.logout();

            List<String> setCookie = resp.getHeaders().get(HttpHeaders.SET_COOKIE);
            if (setCookie != null) {
                setCookie.forEach(cookie -> servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie));
            }

            return ResponseEntity
                    .status(resp.getStatusCode())
                    .body(resp.getBody());
        } catch (FeignException e) {
            log.error("Feign call to logout failed: status={}", e.status(), e);
            return ResponseEntity.status(e.status() > 0 ? e.status() : 503).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String,Object> body = Map.of(
                "email",     jwt.getSubject(),
                "userId",    jwt.getClaimAsString("userId"),
                "roles",     jwt.getClaimAsStringList("roles")
        );
        return ResponseEntity.ok(body);
    }
}