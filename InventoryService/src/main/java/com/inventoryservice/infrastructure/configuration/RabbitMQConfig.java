package com.inventoryservice.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {
    // OrderService exchange/queue routing
    public static final String ORDER_EXCHANGE = "orders.exchange";
    public static final String ORDER_QUEUE    = "inventory.orders.create.queue";
    public static final String ORDER_KEY      = "orders.create";

    // InventoryService exchange/queue routing
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String ADD_QUEUE          = "inventory.add.queue";
    public static final String REMOVE_QUEUE       = "inventory.remove.queue";
    public static final String ADD_KEY            = "inventory.add";
    public static final String REMOVE_KEY         = "inventory.remove";

    // --- Shared Message Converter ---
    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    // --- RabbitTemplate (shared) ---
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(converter);
        return tpl;
    }

    // --- Listener Container Factory (shared) ---
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        return factory;
    }

    // --- OrderService Exchange/Queue/Binding ---
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue)
                .to(orderExchange)
                .with(ORDER_KEY);
    }

    // --- InventoryService Exchange/Queues/Bindings ---
    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Queue inventoryAddQueue() {
        return new Queue(ADD_QUEUE, true);
    }

    @Bean
    public Queue inventoryRemoveQueue() {
        return new Queue(REMOVE_QUEUE, true);
    }

    @Bean
    public Binding inventoryAddBinding(Queue inventoryAddQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(inventoryAddQueue)
                .to(inventoryExchange)
                .with(ADD_KEY);
    }

    @Bean
    public Binding inventoryRemoveBinding(Queue inventoryRemoveQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(inventoryRemoveQueue)
                .to(inventoryExchange)
                .with(REMOVE_KEY);
    }
}
