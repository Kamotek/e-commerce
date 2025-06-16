package com.notificationservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "auth-service", url = "${auth.service.url}")
public interface AuthServiceClient {

    @GetMapping("/auth/users/{userId}")
    UserDto getUser(@PathVariable("userId") UUID userId);

    record UserDto(UUID userId, String email, String[] roles) {}
}
