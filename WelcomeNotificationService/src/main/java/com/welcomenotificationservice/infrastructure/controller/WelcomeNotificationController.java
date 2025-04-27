package com.welcomenotificationservice.infrastructure.controller;

import com.welcomenotificationservice.application.command.handler.CreateWelcomeNotificationCommandHandler;
import com.welcomenotificationservice.application.command.model.CreateWelcomeNotificationCommand;
import com.welcomenotificationservice.domain.model.WelcomeNotification;
import com.welcomenotificationservice.domain.repository.WelcomeNotificationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/welcome-notifications")
@RequiredArgsConstructor
public class WelcomeNotificationController {
    private final CreateWelcomeNotificationCommandHandler createHandler;
    private final WelcomeNotificationRepository repo;

    @PostMapping
    public ResponseEntity<WelcomeNotification> create(
            @Valid @RequestBody CreateWelcomeNotificationCommand cmd
    ) {
        WelcomeNotification saved = createHandler.handle(cmd);
        URI location = URI.create("/api/welcome-notifications/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WelcomeNotification> get(@PathVariable UUID id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<WelcomeNotification>> list() {
        return ResponseEntity.ok(repo.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WelcomeNotification> update(
            @PathVariable UUID id,
            @Valid @RequestBody WelcomeNotification notification
    ) {
        notification.setId(id);
        return ResponseEntity.ok(repo.update(notification));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repo.delete(id);
        return ResponseEntity.noContent().build();
    }
}
