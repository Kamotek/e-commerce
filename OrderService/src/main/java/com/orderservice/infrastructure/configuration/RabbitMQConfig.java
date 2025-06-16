package com.orderservice.infrastructure.configuration;

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


@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "orders.exchange";
    public static final String NOTIFICATION_ORDER_QUEUE    = "notification.orders.create.queue";
    public static final String INVENTORY_ORDER_QUEUE    = "inventory.orders.create.queue";
    public static final String PAYMENT_ORDER_QUEUE    = "payment.orders.create.queue";
    public static final String ORDER_KEY      = "orders.create";

    public static final String PAYMENT_EXCHANGE           = "payments.exchange";
    public static final String PAYMENT_PROCESSED_QUEUE = "orders.payment.processed.queue";
    public static final String PAYMENTS_PROCESSED = "payments.processed";

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue paymentSubmittedQueue() {
        return new Queue(PAYMENT_PROCESSED_QUEUE, true);
    }

    @Bean
    public Binding paymentSubmittedBinding(Queue paymentSubmittedQueue,
                                           TopicExchange paymentExchange) {
        return BindingBuilder
                .bind(paymentSubmittedQueue)
                .to(paymentExchange)
                .with(PAYMENTS_PROCESSED);
    }


    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue(NOTIFICATION_ORDER_QUEUE, true);
    }

    @Bean
    public Queue inventoryOrderQueue() {
        return new Queue(INVENTORY_ORDER_QUEUE, true);
    }

    @Bean
    public Queue paymentOrderQueue() {
        return new Queue(PAYMENT_ORDER_QUEUE, true);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, TopicExchange orderExchange) {
        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with(ORDER_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(converter);
        return tpl;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(converter);
        return f;
    }
}

