package com.inventoryservice.application.command.model;
import lombok.*;
import java.util.UUID;
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class AddStockCommand { private UUID productId; private int amount; }