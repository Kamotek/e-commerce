package com.catalogservice.infrastructure.messaging.consumer;

import com.catalogservice.domain.model.Product;
import com.catalogservice.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdateProductListener {
    @RabbitListener(queues = RabbitMQConfig.UPDATE_QUEUE_NAME)
    public void onUpdateProduct(Map<String, Object> payload) {
        String productId = (String) payload.get("productId");

        System.out.println("Odebrano komunikat updatowania catalogu: " + productId);
    }
}
