package com.orderservice.application.command.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateOrderCommand {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @Valid @NotEmpty(message = "Items list cannot be empty")
    private List<CreateOrderItemCommand> items;

    @NotNull(message = "Order date is required")
    private Instant orderDate;

    private String shippingStreet;

    private String shippingCity;

    private String shippingPostalCode;

    private String shippingCountry;
}