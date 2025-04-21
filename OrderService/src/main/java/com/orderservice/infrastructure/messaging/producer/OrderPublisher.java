package com.orderservice.infrastructure.messaging.producer;

import com.orderservice.application.command.model.CreateOrderCommand;
import com.orderservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishCreateOrder(CreateOrderCommand cmd) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_KEY,
                cmd
        );
    }
}

