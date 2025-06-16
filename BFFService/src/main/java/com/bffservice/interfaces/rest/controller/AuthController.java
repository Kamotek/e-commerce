package com.bffservice.interfaces.rest.controller;


import com.bffservice.application.command.model.LoginUserCommand;
import com.bffservice.application.command.model.RegisterUserCommand;
import com.bffservice.domain.model.AggregatedUser;
import com.bffservice.interfaces.rest.AuthServiceClient;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AuthController {

    private final AuthServiceClient authClient;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterUserCommand cmd
    ) {
        try {
            ResponseEntity<String> resp = authClient.register(cmd);
            return ResponseEntity
                    .status(resp.getStatusCode())
                    .build();
        } catch (FeignException.Unauthorized ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginUserCommand cmd,
            HttpServletResponse servletResponse
    ) {
        ResponseEntity<Map<String, Object>> resp = authClient.login(cmd);

        List<String> setCookie = resp.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (setCookie != null) {
            setCookie.forEach(cookie -> servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie));
        }

        return ResponseEntity
                .status(resp.getStatusCode())
                .body(resp.getBody());
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<AggregatedUser>> getAllUsers() {
        ResponseEntity<List<AggregatedUser>> resp = authClient.getAllUsers();
        return ResponseEntity
                .status(resp.getStatusCode())
                .body(resp.getBody());
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse servletResponse) {
        ResponseEntity<Map<String, Object>> resp = authClient.logout();

        List<String> setCookie = resp.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (setCookie != null) {
            setCookie.forEach(cookie -> servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie));
        }

        return ResponseEntity
                .status(resp.getStatusCode())
                .body(resp.getBody());
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(401).build();
        }
        Map<String,Object> body = Map.of(
                "email",     jwt.getSubject(),
                "userId",    jwt.getClaimAsString("userId"),
                "roles",     jwt.getClaimAsStringList("roles")
        );
        return ResponseEntity.ok(body);
    }
}
