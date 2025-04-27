package com.paymentservice.application.command.handler;

import com.paymentservice.application.command.model.UpdatePaymentCommand;
import com.paymentservice.domain.model.Payment;
import com.paymentservice.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePaymentCommandHandler {
    private final PaymentRepository repo;

    public Payment handle(UpdatePaymentCommand cmd) {
        Payment payment = Payment.builder()
                .paymentId(cmd.getPaymentId())
                .amount(cmd.getAmount())
                .build();
        return repo.updatePayment(payment);
    }
}