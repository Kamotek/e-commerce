package com.bffservice.application.command.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderItemCommand {
    @NotNull(message = "Product ID must not be null")
    private UUID productId;

    @Min(value = 0, message = "Quantity must be at least 0")
    private int quantity;
}