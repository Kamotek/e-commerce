package com.paymentservice.domain.repository;

import com.paymentservice.domain.model.Payment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment createPayment(Payment payment);
    Payment updatePayment(Payment payment);
    void deletePayment(UUID paymentId);
    Optional<Payment> findByPaymentId(UUID paymentId);
    List<Payment> findAllPayments();
}
