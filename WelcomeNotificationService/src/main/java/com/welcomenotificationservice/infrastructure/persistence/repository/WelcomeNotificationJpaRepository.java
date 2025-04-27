package com.welcomenotificationservice.infrastructure.persistence.repository;

import com.welcomenotificationservice.infrastructure.persistence.entity.WelcomeNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WelcomeNotificationJpaRepository
        extends JpaRepository<WelcomeNotificationEntity, UUID> { }
