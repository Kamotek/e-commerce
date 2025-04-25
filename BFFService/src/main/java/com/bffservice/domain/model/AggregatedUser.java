package com.bffservice.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class AggregatedUser {

    @Id
    private UUID id;

    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLogin;

    public AggregatedUser(
            UUID id,
            String email,
            String firstName,
            String lastName,
            LocalDateTime registrationDate,
            LocalDateTime lastLogin
    ) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationDate = registrationDate;
        this.lastLogin = lastLogin;
    }
}