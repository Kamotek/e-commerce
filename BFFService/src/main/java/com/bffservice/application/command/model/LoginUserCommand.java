package com.bffservice.application.command.model;

import lombok.*;


/**
 * Represents a logging user command.
 * This DTO captures the user's credentials from a login request.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserCommand {
    private String email;
    private String password;
}
