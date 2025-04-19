package com.authservice.infrastructure.messaging.consumer;

import com.authservice.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.authservice.application.command.model.RegisterUserCommand;

@Component
public class UserRegistrationListener {

    @RabbitListener(queues = RabbitMQConfig.USER_REG_QUEUE)
    public void onUserRegistered(RegisterUserCommand command) {
        System.out.println("Odebrano komunikat rejestracji: " + command.getEmail());
/// TODO Tutaj mozna dodac jakas logike, typu maila wyslac, czy cos - zastanowie sie nad tym
    }
}
