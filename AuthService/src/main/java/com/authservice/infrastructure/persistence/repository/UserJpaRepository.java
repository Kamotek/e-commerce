    package com.authservice.infrastructure.persistence.repository;

    import com.authservice.infrastructure.persistence.entity.UserEntity;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;
    import java.util.UUID;

    @Repository
    public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
        Optional<UserEntity> findByEmail(String email);
        
    }
