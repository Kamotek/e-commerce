package com.orderservice.application.mapper;

import com.orderservice.domain.model.OrderItem;
import com.orderservice.infrastructure.persistence.entity.OrderItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemEntity toEntity(OrderItem domain);

    OrderItem toDomain(OrderItemEntity entity);
}
