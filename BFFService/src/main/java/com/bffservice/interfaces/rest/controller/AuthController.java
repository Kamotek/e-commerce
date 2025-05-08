package com.bffservice.interfaces.rest.controller;


import com.bffservice.application.command.model.LoginUserCommand;
import com.bffservice.application.command.model.RegisterUserCommand;
import com.bffservice.domain.model.AggregatedUser;
import com.bffservice.interfaces.rest.AuthServiceClient;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @Valid @RequestBody LoginUserCommand cmd
    ) {
        ResponseEntity<Map<String, Object>> resp = authClient.login(cmd);
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
}
