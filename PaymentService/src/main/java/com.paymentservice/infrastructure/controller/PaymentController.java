package com.paymentservice.infrastructure.controller;

import com.paymentservice.application.command.handler.CreatePaymentCommandHandler;
import com.paymentservice.application.command.handler.UpdatePaymentCommandHandler;
import com.paymentservice.application.command.model.CreatePaymentCommand;
import com.paymentservice.application.command.model.UpdatePaymentCommand;
import com.paymentservice.domain.model.Payment;
import com.paymentservice.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final CreatePaymentCommandHandler createHandler;
    private final UpdatePaymentCommandHandler updateHandler;
    private final PaymentRepository repository;

    @PostMapping
    public ResponseEntity<Payment> create(@Valid @RequestBody CreatePaymentCommand cmd) {
        Payment saved = createHandler.handle(cmd);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getPaymentId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> get(@PathVariable UUID id) {
        return repository.findByPaymentId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Payment>> list() {
        return ResponseEntity.ok(repository.findAllPayments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePaymentCommand cmd
    ) {
        cmd.setPaymentId(id);
        return ResponseEntity.ok(updateHandler.handle(cmd));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        repository.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
