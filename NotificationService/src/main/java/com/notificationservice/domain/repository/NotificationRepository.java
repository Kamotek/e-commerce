package com.notificationservice.domain.repository;

import com.notificationservice.domain.model.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    Notification createNotification(Notification notification);
    Notification updateNotification(Notification notification);
    void deleteNotification(UUID notificationId);
    Optional<Notification> findByNotificationId(UUID notificationId);
    List<Notification> findAllNotifications();
}
