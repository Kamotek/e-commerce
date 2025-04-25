package com.bffservice.application.command.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegisteredEvent {
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
}