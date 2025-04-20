package com.inventoryservice.application.query.model;
import lombok.*;import java.util.UUID;
@AllArgsConstructor @Getter
public class StockLevelQuery { private UUID productId; }