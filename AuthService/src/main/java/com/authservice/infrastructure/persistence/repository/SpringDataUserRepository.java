package com.authservice.infrastructure.persistence.repository;

import com.authservice.domain.model.User;
import com.authservice.domain.repository.UserRepository;
import com.authservice.infrastructure.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class SpringDataUserRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Autowired
    public SpringDataUserRepository(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void save(User user) {
        UserEntity entity = new UserEntity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName()
        );
        userJpaRepository.save(entity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(entity -> new User(
                        entity.getId(),
                        entity.getEmail(),
                        entity.getPassword(),
                        entity.getFirstName(),
                        entity.getLastName()
                ));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(entity -> new User(
                        entity.getId(),
                        entity.getEmail(),
                        entity.getPassword(),
                        entity.getFirstName(),
                        entity.getLastName()
                ));
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(
                entity -> new User(
                        entity.getId(),
                        entity.getEmail(),
                        entity.getPassword(),
                        entity.getFirstName(),
                        entity.getLastName()))
                .collect(Collectors.toList());

    }

}
