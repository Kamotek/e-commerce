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
    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public Notification createNotification(Notification notification) {
        NotificationEntity entity = NotificationEntity.builder()
                .notificationId(UUID.randomUUID())
                .userEmail(notification.getUserEmail())
                .title(notification.getTitle())
                .body(notification.getBody())
                .build();
        notificationJpaRepository.save(entity);
        return notification;
    }

    @Override
    public Notification updateNotification(Notification notification) {
        NotificationEntity entity = notificationJpaRepository.findById(notification.getNotificationId()).orElse(null);
        entity.setTitle(notification.getTitle());
        entity.setBody(notification.getBody());
        notificationJpaRepository.save(entity);
        return notification;
    }

    @Override
    public void deleteNotification(UUID notificationId) {
        notificationJpaRepository.delete(notificationJpaRepository.findById(notificationId).orElse(null));
    }

    @Override
    public Optional<Notification> findByNotificationId(UUID notificationId) {
        return notificationJpaRepository.findById(notificationId).map(notificationMapper::toDomain);
    }

    @Override
    public List<Notification> findAllNotifications() {
        return notificationJpaRepository.findAll().stream()
                .map(notificationMapper::toDomain)
                .collect(Collectors.toList());
    }


}
