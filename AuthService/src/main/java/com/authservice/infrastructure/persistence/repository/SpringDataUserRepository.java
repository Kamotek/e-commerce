package com.authservice.infrastructure.persistence.repository;

import com.authservice.domain.model.User;
import com.authservice.domain.repository.UserRepository;
import com.authservice.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SpringDataUserRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public void save(User user) {
        UserEntity entity = new UserEntity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt()
        );
        userJpaRepository.save(entity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private User toDomain(UserEntity e) {
        return User.builder()
                .id(e.getId())
                .email(e.getEmail())
                .password(e.getPassword())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
