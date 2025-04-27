package com.paymentservice.infrastructure.messaging.producer;

import com.paymentservice.application.command.model.PaymentSubmittedEvent;
import com.paymentservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPublisher {
    private final RabbitTemplate tpl;

    public void publish(PaymentSubmittedEvent evt) {
        tpl.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_ROUTING_KEY,
                evt
        );
    }
}
