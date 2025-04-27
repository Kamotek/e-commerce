package com.welcomenotificationservice.domain.repository;

import com.welcomenotificationservice.domain.model.WelcomeNotification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WelcomeNotificationRepository {
    WelcomeNotification create(WelcomeNotification notification);
    WelcomeNotification update(WelcomeNotification notification);
    void delete(UUID id);
    Optional<WelcomeNotification> findById(UUID id);
    List<WelcomeNotification> findAll();
}
