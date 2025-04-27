package com.orderservice.application.mapper;

import com.orderservice.application.command.model.CreateOrderCommand;
import com.orderservice.domain.model.Order;
import com.orderservice.domain.model.OrderItem;
import com.orderservice.infrastructure.persistence.entity.OrderEntity;
import com.orderservice.infrastructure.persistence.entity.OrderItemEntity;
import org.mapstruct.*;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { OrderItemMapper.class }
)
@Primary
public interface OrderMapper {

    @Mapping(target = "items", source = "entities.items")
    Order toDomain(OrderEntity entities);

    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "items", ignore = true)
    OrderEntity toEntity(Order domain);

    default List<OrderItem> mapItemsToDomain(List<OrderItemEntity> entities) {
        return entities.stream()
                .map(e -> OrderItem.builder()
                        .productId(e.getProductId())
                        .quantity(e.getQuantity())
                        .build())
                .toList();
    }

    default List<OrderItemEntity> mapItemsToEntity(List<OrderItem> items, @Context OrderEntity parent) {
        return items.stream()
                .map(i -> {
                    OrderItemEntity e = new OrderItemEntity();
                    e.setOrder(parent);
                    e.setProductId(i.getProductId());
                    e.setQuantity(i.getQuantity());
                    return e;
                })
                .toList();
    }
}
