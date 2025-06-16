package com.bffservice.interfaces.rest;

import com.bffservice.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "payment-service", url = "${payment.service.url}", configuration = FeignConfig.class)
public interface PaymentServiceClient {

    @PostMapping("/api/payments/{orderId}/process")
    ResponseEntity<ProcessPaymentResponse> processPayment(
            @PathVariable("orderId") UUID orderId,
            @RequestBody ProcessPaymentRequest request
    );

    record ProcessPaymentRequest(String cardNumber) {}
    record ProcessPaymentResponse(String paymentStatus) {}
}