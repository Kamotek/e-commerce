package com.bffservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.spec.SecretKeySpec;

/**
 * Configures JWT handling for Spring Security.
 * This class is responsible for setting up the beans required to decode and validate JWT,
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Creates a {@link JwtDecoder} bean responsible for validating the signature of incoming JWTs.
     * It uses a symmetric secret key and the HMAC-SHA256 algorithm to ensure the token's
     * integrity and authenticity.
     *
     * @return A configured {@code JwtDecoder} instance for Spring Security to use.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(
                new SecretKeySpec(secret.getBytes(), "HmacSHA256")).build();
    }

    /**
     * Creates a {@link JwtAuthenticationConverter} bean that converts a validated JWT into a
     * Spring Security {@link org.springframework.security.core.Authentication} object.

     * This converter is specifically configured to:
     *     Extract user authorities (roles) from the "roles" claim within the JWT.
     *     Disable the default "ROLE_" prefix for authorities, allowing for simpler role names (e.g., "ADMIN" instead of "ROLE_ADMIN").
     *
     * @return A configured {@code JwtAuthenticationConverter} instance for Spring Security to use.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }
}