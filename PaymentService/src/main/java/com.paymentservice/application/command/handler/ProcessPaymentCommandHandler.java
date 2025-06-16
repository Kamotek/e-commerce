package com.paymentservice.application.command.handler;

import com.paymentservice.application.command.model.ProcessPaymentCommand;
import com.paymentservice.application.command.model.PaymentProcessedEvent; // <-- IMPORT
import com.paymentservice.domain.model.Payment;
import com.paymentservice.domain.repository.PaymentRepository;
import com.paymentservice.infrastructure.messaging.producer.PaymentPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessPaymentCommandHandler {
    private final PaymentRepository paymentRepository;
    private final PaymentPublisher paymentPublisher;
    private final Random random = new Random();

    public boolean handle(ProcessPaymentCommand command) {
        Payment payment = paymentRepository.findByOrderId(command.orderId())
                .orElseThrow(() -> new IllegalStateException("Payment for order not found"));

        boolean paymentSuccess = command.cardNumber() != null && command.cardNumber().startsWith("1111") || random.nextBoolean();

        payment.setStatus(paymentSuccess ? Payment.PaymentStatus.COMPLETED : Payment.PaymentStatus.FAILED);
        paymentRepository.updatePayment(payment);
        log.info("Payment {} for order {} processed with status: {}", payment.getPaymentId(), command.orderId(), payment.getStatus());

        paymentPublisher.publishPaymentProcessed(
                new PaymentProcessedEvent(
                        payment.getOrderId(),
                        payment.getPaymentId(),
                        payment.getUserId(),
                        payment.getStatus().name()
                )
        );

        return paymentSuccess;
    }
}