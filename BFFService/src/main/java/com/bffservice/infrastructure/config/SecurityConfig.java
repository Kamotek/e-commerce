package com.bffservice.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // BFF jest stateless
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // reguły dostępu
                .authorizeHttpRequests(auth -> auth
                        // === AUTH endpoints (publiczne) ===
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/auth/allUsers"
                        ).permitAll()

                        // === CATALOG endpoints (publiczne) ===
                        .requestMatchers(HttpMethod.GET,
                                "/api/catalog/products",
                                "/api/catalog/products/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/catalog/products"
                        ).permitAll()

                        // === ORDERS endpoints (publiczne) ===
                        .requestMatchers(HttpMethod.GET,
                                "/api/orders",
                                "/api/orders/**"
                        ).permitAll()

                        // jeśli chcesz, żeby tworzenie/modyfikacja zamówień też było publiczne
                        // możesz dodać POST/PUT/DELETE tutaj; w przeciwnym razie zostaną one chronione
                         .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()
                         .requestMatchers(HttpMethod.PUT, "/api/orders/**").permitAll()
                         .requestMatchers(HttpMethod.DELETE, "/api/orders/**").permitAll()

                        // wszystkie inne żądania wymagają uwierzytelnienia
                        .anyRequest().authenticated()
                )
                // JWT Resource Server
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                );

        return http.build();
    }
}
