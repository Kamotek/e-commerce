package com.bffservice.application.command.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateOrderCommand {
    @NotNull(message = "User ID must not be null")
    private UUID userId;

    @Valid @NotEmpty(message = "Order must contain at least one item")
    private List<CreateOrderItemCommand> items;

    @NotNull(message = "Order date must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant orderDate;

    @NotBlank(message = "Shipping street must not be blank")
    private String shippingStreet;

    @NotBlank(message = "Shipping city must not be blank")
    private String shippingCity;

    @NotBlank(message = "Shipping postal code must not be blank")
    private String shippingPostalCode;

    @NotBlank(message = "Shipping country must not be blank")
    private String shippingCountry;
}
