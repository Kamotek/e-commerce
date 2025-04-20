package com.inventoryservice.application.dto;

import com.inventoryservice.domain.model.StockItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateItemRequest {
    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;

    @NotBlank(message = "Category must not be blank")
    @Size(max = 100, message = "Category must be at most 100 characters")
    private String category;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @Min(value = 0, message = "Initial quantity must be at least 0")
    private int initialQuantity;

    public StockItem toDomain() {
        return StockItem.builder()
                .productId(null)
                .name(name)
                .category(category)
                .description(description)
                .quantity(initialQuantity)
                .build();
    }
}
