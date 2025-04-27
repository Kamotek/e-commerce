package com.authservice.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    private UUID id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Instant createdAt;

    public User(String email, String password, String firstName, String lastName) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
