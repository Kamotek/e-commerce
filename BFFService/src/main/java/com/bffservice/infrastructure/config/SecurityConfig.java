package com.bffservice.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                // 1) Wyłącz CSRF i sesje
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 2) Zezwól zawsze na register/login/allUsers
                .authorizeHttpRequests(auth -> auth
                        // publiczne endpointy BFF:
                        .requestMatchers(HttpMethod.POST,   "/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET,    "/api/auth/allUsers").permitAll()
                        // publiczne katalog i zamówienia:
                        .requestMatchers("/api/catalog/**").permitAll()
                        .requestMatchers("/api/orders/**", "/api/orders").permitAll()
                        // wszystkie pozostałe muszą być autoryzowane
                        .anyRequest().authenticated()
                )

                // 3) Teraz włącz JWT Resource Server
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                );

        return http.build();
    }
}
