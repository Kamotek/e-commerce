package com.welcomenotificationservice.infrastructure.persistence.repository;

import com.welcomenotificationservice.domain.model.WelcomeNotification;
import com.welcomenotificationservice.domain.repository.WelcomeNotificationRepository;
import com.welcomenotificationservice.infrastructure.persistence.entity.WelcomeNotificationEntity;
import com.welcomenotificationservice.infrastructure.persistence.repository.WelcomeNotificationJpaRepository;
import com.welcomenotificationservice.application.mapper.WelcomeNotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpringDataWelcomeNotificationRepository
        implements WelcomeNotificationRepository {

    private final WelcomeNotificationJpaRepository jpa;

    @Qualifier("welcomeNotificationMapper")
    private final WelcomeNotificationMapper mapper;

    @Override
    public WelcomeNotification create(WelcomeNotification n) {
        if (n.getId() == null) n.setId(UUID.randomUUID());
        WelcomeNotificationEntity e = mapper.toEntity(n);
        e = jpa.save(e);
        return mapper.toDomain(e);
    }

    @Override
    public WelcomeNotification update(WelcomeNotification n) {
        if (!jpa.existsById(n.getId())) {
            throw new IllegalArgumentException("No notification " + n.getId());
        }
        WelcomeNotificationEntity e = mapper.toEntity(n);
        e = jpa.save(e);
        return mapper.toDomain(e);
    }

    @Override
    public void delete(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<WelcomeNotification> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<WelcomeNotification> findAll() {
        return jpa.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
