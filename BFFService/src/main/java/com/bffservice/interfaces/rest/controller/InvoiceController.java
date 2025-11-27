package com.bffservice.interfaces.rest.controller;

import com.bffservice.interfaces.rest.InvoiceServiceClient;
import com.bffservice.interfaces.rest.OrderServiceClient;

import ch.qos.logback.classic.Logger;

import com.bffservice.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;



@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceServiceClient invoiceServiceClient;
    private final OrderServiceClient orderServiceClient;

    Logger logger = (Logger) org.slf4j.LoggerFactory.getLogger(InvoiceController.class);

    @GetMapping("/{orderId}/pdf")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable UUID orderId, @RequestParam String email) throws Exception {
        ResponseEntity<Order> orderResp = orderServiceClient.getOrder(orderId);
        if (!orderResp.getStatusCode().is2xxSuccessful() || orderResp.getBody() == null) {
            return ResponseEntity.notFound().build();
        }
        Order order = orderResp.getBody();

        byte[] pdfBytes = invoiceServiceClient.generateInvoice(order, email);
        System.out.println("PDF bytes length: " + pdfBytes.length);
        
        logger.info("Generated PDF invoice for order ID: {}", orderId);
        logger.info("Generated PDF invoice for order ID: " + orderId);
        logger.info("PDF size: {} bytes", pdfBytes.length);

        return ResponseEntity.ok()
            .header("Content-Type", "application/pdf")
            .header("Content-Disposition", "attachment; filename=invoice-" + orderId + ".pdf")
            .header("Content-Length", String.valueOf(pdfBytes.length))
            .body(pdfBytes);
    }
}