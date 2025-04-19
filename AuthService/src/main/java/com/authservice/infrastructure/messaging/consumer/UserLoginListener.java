package com.authservice.infrastructure.messaging.consumer;

import com.authservice.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserLoginListener {
    @RabbitListener(queues = RabbitMQConfig.USER_LOGIN_QUEUE)
    public void onUserLogin(Map<String, Object> payload) {
        String userId = (String) payload.get("userId");
        String password = (String) payload.get("password");
        String email = (String) payload.get("email");

        System.out.println("Odebrano komunikat logowania " + userId + " " + email);
    }
}
