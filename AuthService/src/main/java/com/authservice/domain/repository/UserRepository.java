package com.authservice.domain.repository;

import com.authservice.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    List<User> findAll();
    void updateUserRole(UUID userId, String role);
}
