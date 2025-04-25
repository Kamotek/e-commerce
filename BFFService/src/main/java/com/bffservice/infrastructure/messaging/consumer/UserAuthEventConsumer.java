package com.bffservice.infrastructure.messaging.consumer;

import com.bffservice.application.command.handler.UserAuthEventHandler;
import com.bffservice.application.command.model.UserLoggedInEvent;
import com.bffservice.application.command.model.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


// TODO - Do wywalenia
@Component
@RequiredArgsConstructor
public class UserAuthEventConsumer {
    private final UserAuthEventHandler handler;

    @RabbitListener(queues = "#{@evtRegisterQueue.name}")
    public void onRegistered(UserRegisteredEvent e) {
        handler.handleRegistration(e);
    }

    @RabbitListener(queues = "#{@evtLoginQueue.name}")
    public void onLoggedIn(UserLoggedInEvent e) {
        handler.handleLogin(e);
    }
}