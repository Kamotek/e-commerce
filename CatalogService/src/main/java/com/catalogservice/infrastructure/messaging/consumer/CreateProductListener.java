package com.catalogservice.infrastructure.messaging.consumer;

import com.catalogservice.infrastructure.configuration.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreateProductListener {
    @RabbitListener(queues = RabbitMQConfig.CREATE_QUEUE_NAME)
    public void onCreateProduct(Map<String, Object> payload) {
        String productId = (String) payload.get("productId");
        String productName = (String) payload.get("productName");
        String productDescription = (String) payload.get("productDescription");
        String productPrice = (String) payload.get("productPrice");

        System.out.println("Odebrano komunikat tworzenia produktu w katalogu");
    }
}
