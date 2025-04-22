package com.notificationservice.application.mapper;

import com.notificationservice.domain.model.Notification;
import com.notificationservice.infrastructure.persistence.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toDomain(NotificationEntity entity);

    @Mapping(target = "notificationId", expression = "java(notification.getNotificationId() != null ? notification.getNotificationId() : java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt",   expression = "java(notification.getCreatedAt() != null ? notification.getCreatedAt() : java.time.Instant.now())")
    NotificationEntity toEntity(Notification notification);
}
