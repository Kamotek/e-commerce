package com.bffservice.application.command.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserCommand {
    private String email;
    private String password;
}
