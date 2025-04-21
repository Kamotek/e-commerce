package com.orderservice.application.command.model;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderCommand {
    @NotNull(message = "User ID must not be null")
    private UUID userId;

    @Valid
    @NotEmpty(message = "Order must contain at least one item")
    private List<CreateOrderItemCommand> items;

    @Valid
    private Instant orderDate;
}
