package com.authservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class User {
    private UUID id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public User(String email, String password, String firstName, String lastName) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }


}
