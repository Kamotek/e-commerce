package com.orderservice.application.mapper;

import com.orderservice.domain.model.*;
import com.orderservice.infrastructure.persistence.entity.*;
import org.mapstruct.*;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper(componentModel = "spring", uses = { OrderItemMapper.class })
@Primary
public interface OrderMapper {

    @Mapping(target = "items",             source = "entities.items")
    @Mapping(target = "shippingStreet",   source = "entities.shippingStreet")
    @Mapping(target = "shippingCity",     source = "entities.shippingCity")
    @Mapping(target = "shippingPostalCode", source = "entities.shippingPostalCode")
    @Mapping(target = "shippingCountry",  source = "entities.shippingCountry")
    @Mapping(target = "paymentStatus",    source = "entities.paymentStatus")
    @Mapping(target = "totalAmount",    source = "entities.totalAmount")
    Order toDomain(OrderEntity entities);

    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "items",     ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "shippingStreet",   source = "domain.shippingStreet")
    @Mapping(target = "shippingCity",     source = "domain.shippingCity")
    @Mapping(target = "shippingPostalCode", source = "domain.shippingPostalCode")
    @Mapping(target = "shippingCountry",  source = "domain.shippingCountry")
    @Mapping(target = "totalAmount",      source = "domain.totalAmount")
    OrderEntity toEntity(Order domain);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "items", ignore = true)
    void updateEntityFromDomain(Order domain, @MappingTarget OrderEntity entity);


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
