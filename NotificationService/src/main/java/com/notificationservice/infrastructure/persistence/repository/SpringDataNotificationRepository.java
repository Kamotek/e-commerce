package com.notificationservice.infrastructure.persistence.repository;

import com.notificationservice.application.mapper.NotificationMapper;
import com.notificationservice.domain.model.Notification;
import com.notificationservice.domain.repository.NotificationRepository;
import com.notificationservice.infrastructure.persistence.entity.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpringDataNotificationRepository implements NotificationRepository {
    private final NotificationJpaRepository jpa;
    private final NotificationMapper mapper;

    @Override
    public Notification createNotification(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        NotificationEntity saved   = jpa.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Notification updateNotification(Notification notification) {
        UUID id = notification.getNotificationId();
        NotificationEntity existing = jpa.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No notification with id " + id));

        existing.setTitle(notification.getTitle());
        existing.setBody(notification.getBody());
        NotificationEntity saved = jpa.save(existing);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteNotification(UUID notificationId) {
        if (!jpa.existsById(notificationId)) {
            throw new IllegalArgumentException("No notification with id " + notificationId);
        }
        jpa.deleteById(notificationId);
    }

    @Override
    public Optional<Notification> findByNotificationId(UUID notificationId) {
        return jpa.findById(notificationId).map(mapper::toDomain);
    }

    @Override
    public List<Notification> findAllNotifications() {
        return jpa.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
