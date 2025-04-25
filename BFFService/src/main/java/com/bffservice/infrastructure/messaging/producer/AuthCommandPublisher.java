package com.bffservice.infrastructure.messaging.producer;

import com.bffservice.application.command.model.LoginUserCommand;
import com.bffservice.application.command.model.RegisterUserCommand;
import com.bffservice.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


// TODO - Do wywalenia

@Service
@RequiredArgsConstructor
public class AuthCommandPublisher {
    private final RabbitTemplate rabbit;

    public void sendRegister(RegisterUserCommand cmd) {
        rabbit.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.CMD_REG,
                cmd
        );
    }

    public void sendLogin(LoginUserCommand cmd) {
        rabbit.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.CMD_LOGIN,
                cmd
        );
    }
}