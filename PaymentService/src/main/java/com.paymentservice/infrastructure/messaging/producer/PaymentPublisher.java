package com.paymentservice.infrastructure.messaging.producer;

import com.paymentservice.application.command.model.PaymentProcessedEvent;
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


    public void publishPaymentProcessed(PaymentProcessedEvent event) {
        log.info("Publishing payment processed event: {}", event);
        tpl.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_PROCESSED_ROUTING_KEY,
                event
        );
    }
}
