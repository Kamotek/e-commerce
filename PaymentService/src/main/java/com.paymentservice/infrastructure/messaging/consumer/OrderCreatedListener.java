
package com.paymentservice.infrastructure.messaging.consumer;

import com.paymentservice.application.command.handler.CreatePaymentCommandHandler;
import com.paymentservice.application.command.model.CreateOrderEvent;
import com.paymentservice.application.command.model.CreatePaymentCommand;
import com.paymentservice.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedListener {
    private final CreatePaymentCommandHandler handler;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void onOrderCreated(CreateOrderEvent event) {
        handler.handle(
                CreatePaymentCommand.builder()
                        .userId(event.getUserId())
                        .orderId(event.getOrderId())
                        .amount(event.getTotalAmount())
                        .createdAt(event.getOrderDate())
                        .build()
        );

        log.info("Payment created for order {}", event.getOrderId());
    }
}