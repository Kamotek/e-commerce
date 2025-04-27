package com.paymentservice.application.mapper;

import com.paymentservice.domain.model.Payment;
import com.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toDomain(PaymentEntity entity);

    @Mapping(target = "paymentId", expression = "java(payment.getPaymentId() != null ? payment.getPaymentId() : java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(payment.getCreatedAt() != null ? payment.getCreatedAt() : java.time.Instant.now())")
    PaymentEntity toEntity(Payment payment);
}