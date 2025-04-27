package com.paymentservice.infrastructure.messaging.producer;

import com.paymentservice.application.command.model.PaymentSubmittedEvent;
import com.paymentservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentPublisher {
    private final RabbitTemplate tpl;

    public void publish(PaymentSubmittedEvent evt) {
        log.info("Publishing event {}", evt);
        tpl.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_ROUTING_KEY,
                evt
        );
    }
}
