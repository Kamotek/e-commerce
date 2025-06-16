package com.orderservice.infrastructure.messaging.consumer;

import com.orderservice.domain.model.Order;
import com.orderservice.domain.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProcessedListener {

    private final OrderRepository orderRepository;

    @RabbitListener(queues = "orders.payment_processed.queue")
    public void onPaymentProcessed(PaymentProcessedEvent event) {
        log.info("Received payment processed event for orderId: {}", event.getOrderId());

        orderRepository.findByOrderId(event.getOrderId()).ifPresent(order -> {
            Order.PaymentStatus newStatus = switch(event.getStatus()) {
                case "COMPLETED" -> Order.PaymentStatus.PAID;
                case "FAILED" -> Order.PaymentStatus.FAILED;
                default -> order.getPaymentStatus();
            };

            log.info("Updating order {} status to {}", order.getId(), newStatus);
            order.setPaymentStatus(newStatus);
            order.setFinished(newStatus == Order.PaymentStatus.PAID);
            orderRepository.updateOrder(order);
        });
    }

    @Data
    @AllArgsConstructor
    public static class PaymentProcessedEvent {
        private UUID orderId;
        private UUID paymentId;
        private String status;
    }
}