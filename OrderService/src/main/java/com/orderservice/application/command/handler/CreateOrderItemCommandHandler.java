package com.orderservice.application.command.handler;

import com.orderservice.application.command.model.CreateOrderItemCommand;
import com.orderservice.domain.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderItemCommandHandler {


    public OrderItem toDomain(CreateOrderItemCommand command) {
        return OrderItem.builder()
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .build();
    }
}
