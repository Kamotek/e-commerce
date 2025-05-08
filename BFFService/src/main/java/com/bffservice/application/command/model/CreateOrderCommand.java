package com.bffservice.application.command.model;

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

    @NotNull(message = "Order date must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant orderDate;
}