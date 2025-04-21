package com.orderservice.infrastructure.messaging.consumer;

import com.orderservice.application.command.model.CreateOrderCommand;
import com.orderservice.application.mapper.OrderMapper;
import com.orderservice.domain.model.Order;
import com.orderservice.domain.repository.OrderRepository;
import com.orderservice.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {

    private final OrderRepository orderRepo;
    private final OrderMapper mapper;

    public OrderListener(OrderRepository orderRepo, OrderMapper mapper) {
        this.orderRepo = orderRepo;
        this.mapper = mapper;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void onCreateOrder(CreateOrderCommand cmd) {
        Order order = mapper.toDomain(cmd);
        orderRepo.createOrder(order);
    }
}
