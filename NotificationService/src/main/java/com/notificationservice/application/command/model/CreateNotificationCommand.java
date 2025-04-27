package com.notificationservice.application.command.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNotificationCommand {
    @NotBlank
    private String userEmail;

    @NotBlank
    private String title;

    @NotBlank
    private String body;
}
