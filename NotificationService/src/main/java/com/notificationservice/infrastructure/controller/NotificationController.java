package com.notificationservice.infrastructure.controller;

import com.notificationservice.application.command.model.CreateNotificationCommand;
import com.notificationservice.application.command.handler.CreateNotificationCommandHandler;
import com.notificationservice.domain.model.Notification;
import com.notificationservice.domain.repository.NotificationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final CreateNotificationCommandHandler createHandler;
    private final NotificationRepository repo;

    @PostMapping
    public ResponseEntity<Notification> create(
            @Valid @RequestBody CreateNotificationCommand cmd) {
        Notification saved = createHandler.handle(cmd);
        URI location = URI.create("/api/notifications/" + saved.getNotificationId());
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> get(@PathVariable UUID id) {
        return repo.findByNotificationId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Notification>> list() {
        return ResponseEntity.ok(repo.findAllNotifications());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(
            @PathVariable UUID id,
            @Valid @RequestBody Notification notification) {
        notification.setNotificationId(id);
        return ResponseEntity.ok(repo.updateNotification(notification));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repo.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
