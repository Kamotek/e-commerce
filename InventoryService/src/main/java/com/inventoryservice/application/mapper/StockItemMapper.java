// src/main/java/com/inventoryservice/application/mapper/StockItemMapper.java
package com.inventoryservice.application.mapper;

import com.inventoryservice.domain.model.StockItem;
import com.inventoryservice.application.dto.StockItemDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockItemMapper {
    StockItemDto toDto(StockItem model);
}