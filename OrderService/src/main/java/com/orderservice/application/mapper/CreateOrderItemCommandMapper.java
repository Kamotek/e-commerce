package com.orderservice.application.mapper;

import com.orderservice.application.command.model.CreateOrderItemCommand;
import com.orderservice.domain.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateOrderItemCommandMapper {
    OrderItem toDomain(CreateOrderItemCommand cmd);
}
