package com.bffservice.interfaces.rest;

import com.bffservice.application.command.model.LoginUserCommand;
import com.bffservice.application.command.model.RegisterUserCommand;
import com.bffservice.domain.model.AggregatedUser;
import com.bffservice.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "auth-service", url = "${auth.service.url}", configuration = FeignConfig.class)
public interface AuthServiceClient {

    @PostMapping("/auth/register")
    ResponseEntity<String> register(@RequestBody RegisterUserCommand command);

    @PostMapping("/auth/login")
    ResponseEntity<Map<String, Object>> login(@RequestBody LoginUserCommand command);

    @GetMapping("/auth/allUsers")
    ResponseEntity<List<AggregatedUser>> getAllUsers();

    @PostMapping("/auth/logout")
    ResponseEntity<Map<String, Object>> logout();

    @PutMapping("/auth/setAdmin/{id}")
    ResponseEntity<Void> setAdmin(@PathVariable("id") String id);
}
