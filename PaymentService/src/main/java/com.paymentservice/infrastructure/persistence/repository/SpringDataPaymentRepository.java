package com.paymentservice.infrastructure.persistence.repository;

import com.paymentservice.application.mapper.PaymentMapper;
import com.paymentservice.domain.model.Payment;
import com.paymentservice.domain.repository.PaymentRepository;
import com.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpringDataPaymentRepository implements PaymentRepository {
    private final PaymentJpaRepository jpa;
    private final PaymentMapper mapper;

    @Override
    public Payment createPayment(Payment payment) {
        PaymentEntity entity = mapper.toEntity(payment);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return jpa.findByOrderId(orderId).map(mapper::toDomain);
    }

    @Override
    public Payment updatePayment(Payment payment) {
        if (!jpa.existsById(payment.getPaymentId())) {
            throw new IllegalArgumentException("No payment with id " + payment.getPaymentId());
        }
        PaymentEntity entity = mapper.toEntity(payment);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public void deletePayment(UUID paymentId) {
        jpa.deleteById(paymentId);
    }

    @Override
    public Optional<Payment> findByPaymentId(UUID paymentId) {
        return jpa.findById(paymentId).map(mapper::toDomain);
    }

    @Override
    public List<Payment> findAllPayments() {
        return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }
}