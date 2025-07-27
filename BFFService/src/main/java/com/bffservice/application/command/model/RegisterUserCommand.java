package com.bffservice.application.command.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Represents register user request
 * captures user credentials from register request
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterUserCommand {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}