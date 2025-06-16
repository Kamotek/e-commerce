package com.bffservice.interfaces.rest.controller;

import com.bffservice.interfaces.rest.PaymentServiceClient;
import com.bffservice.interfaces.rest.PaymentServiceClient.ProcessPaymentRequest;
import com.bffservice.interfaces.rest.PaymentServiceClient.ProcessPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceClient paymentClient;

    @PostMapping("/{orderId}/process")
    public ResponseEntity<ProcessPaymentResponse> processPayment(
            @PathVariable UUID orderId,
            @RequestBody ProcessPaymentRequest request) {

        return paymentClient.processPayment(orderId, request);
    }
}