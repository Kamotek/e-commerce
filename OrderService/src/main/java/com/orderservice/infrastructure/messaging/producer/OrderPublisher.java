package com.orderservice.infrastructure.messaging.producer;

import com.orderservice.application.command.model.CreateOrderEvent;
import com.orderservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishCreateOrder(CreateOrderEvent event) {
        log.info("Publishing create order event");
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_KEY,
                event
        );
    }
}
