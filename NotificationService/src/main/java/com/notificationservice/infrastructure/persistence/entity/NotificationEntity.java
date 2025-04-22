package com.notificationservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="notifications")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class NotificationEntity {
    @Id
    @Column(name="id", nullable=false, unique=true)
    private UUID notificationId;
    @Column(name="user_email", nullable=false)
    private String userEmail;
    @Column(nullable=false)
    private String title;
    @Column(nullable=false)
    private String body;
    @Column(name="created_at",nullable=false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
