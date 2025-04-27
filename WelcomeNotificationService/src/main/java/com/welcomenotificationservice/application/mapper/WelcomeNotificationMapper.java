package com.welcomenotificationservice.application.mapper;

import com.welcomenotificationservice.domain.model.WelcomeNotification;
import com.welcomenotificationservice.infrastructure.persistence.entity.WelcomeNotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WelcomeNotificationMapper {
    WelcomeNotification toDomain(WelcomeNotificationEntity e);

    @Mapping(target = "id",
            expression = "java(n.getId()!=null ? n.getId() : java.util.UUID.randomUUID())")
    WelcomeNotificationEntity toEntity(WelcomeNotification n);
}
