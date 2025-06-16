package com.paymentservice.infrastructure.controller;

import com.paymentservice.application.command.handler.ProcessPaymentCommandHandler;
import com.paymentservice.application.command.model.ProcessPaymentCommand;
import com.paymentservice.domain.model.Payment;
import com.paymentservice.domain.repository.PaymentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ProcessPaymentCommandHandler processPaymentHandler;
    private final PaymentRepository repository;

    @GetMapping("/{id}")
    public ResponseEntity<Payment> get(@PathVariable UUID id) {
        return repository.findByPaymentId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{orderId}/process")
    public ResponseEntity<ProcessPaymentResponse> processPayment(
            @PathVariable UUID orderId,
            @RequestBody ProcessPaymentRequest request) {

        ProcessPaymentCommand command = new ProcessPaymentCommand(orderId, request.cardNumber);
        boolean success = processPaymentHandler.handle(command);

        String status = success ? "COMPLETED" : "FAILED";
        return ResponseEntity.ok(new ProcessPaymentResponse(status));
    }

    record ProcessPaymentRequest(String cardNumber) {}
    record ProcessPaymentResponse(String paymentStatus) {}
}