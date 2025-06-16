package com.paymentservice.application.command.handler;

import com.paymentservice.application.command.model.CreatePaymentCommand;
import com.paymentservice.domain.model.Payment;
import com.paymentservice.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePaymentCommandHandler {
    private final PaymentRepository repo;

    public void handle(CreatePaymentCommand cmd) {
        Payment saved = repo.createPayment(
                Payment.builder()
                        .userId(cmd.getUserId())
                        .orderId(cmd.getOrderId())
                        .amount(cmd.getAmount())
                        .createdAt(cmd.getCreatedAt())
                        .build()
        );

    }
}
