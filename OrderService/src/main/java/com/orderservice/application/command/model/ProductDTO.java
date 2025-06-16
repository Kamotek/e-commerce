package com.orderservice.application.command.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductDTO {
    private UUID id;
    private BigDecimal price;
}