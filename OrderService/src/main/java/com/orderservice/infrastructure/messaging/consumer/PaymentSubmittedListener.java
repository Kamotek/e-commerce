package com.orderservice.infrastructure.messaging.consumer;

import com.orderservice.application.command.model.PaymentSubmittedEvent;
import com.orderservice.infrastructure.configuration.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentSubmittedListener {

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_SUBMITTED_QUEUE)
    public void onPaymentSubmitted(PaymentSubmittedEvent event) {
        log.info("Received Payment Submitted Event {} at {}", event, event.getCreatedAt());
    }
}
