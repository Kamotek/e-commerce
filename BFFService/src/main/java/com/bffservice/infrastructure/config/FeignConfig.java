package com.bffservice.infrastructure.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * Configuration for Feign clients.
 * This class sets up beans that customize the behavior of Feign, particularly for handling authentication.
 */
@Configuration
public class FeignConfig {
    /**
     * Creates a Feign RequestInterceptor that propagates the Authorization header from the incoming
     * HTTP request to the outgoing Feign request.
     *
     * @return A {@link RequestInterceptor} bean.
     */
    @Bean
    public RequestInterceptor bearerAuthInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                String auth = attrs.getRequest().getHeader("Authorization");
                if (auth != null) {
                    requestTemplate.header("Authorization", auth);
                }
            }
        };
    }
}
