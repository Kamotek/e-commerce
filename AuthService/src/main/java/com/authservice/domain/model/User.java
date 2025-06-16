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
    private String role;
}
