package com.paymentservice.application.command.handler;

import com.paymentservice.application.command.model.CreatePaymentCommand;
import com.paymentservice.application.command.model.PaymentSubmittedEvent;
import com.paymentservice.domain.model.Payment;
import com.paymentservice.domain.repository.PaymentRepository;
import com.paymentservice.infrastructure.messaging.producer.PaymentPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePaymentCommandHandler {
    private final PaymentRepository repo;
    private final PaymentPublisher publisher;

    public Payment handle(CreatePaymentCommand cmd) {
        Payment saved = repo.createPayment(
                Payment.builder()
                        .userId(cmd.getUserId())
                        .orderId(cmd.getOrderId())
                        .amount(cmd.getAmount())
                        .createdAt(cmd.getCreatedAt())
                        .build()
        );

        publisher.publish(
                PaymentSubmittedEvent.builder()
                        .paymentId(saved.getPaymentId())
                        .createdAt(saved.getCreatedAt())
                        .build()
        );

        return saved;
    }
}
