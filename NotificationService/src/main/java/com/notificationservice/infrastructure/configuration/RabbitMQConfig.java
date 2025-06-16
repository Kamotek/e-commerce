package com.notificationservice.infrastructure.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;     // <- to
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@EnableRabbit
@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE       = "notifications.exchange";
    public static final String CREATE_QUEUE   = "notifications.create.queue";
    public static final String CREATE_ROUTING = "notifications.create";

    public static final String ORDER_EXCHANGE = "orders.exchange";
    public static final String ORDER_QUEUE    = "notification.orders.create.queue";
    public static final String ORDER_KEY      = "orders.create";

    public static final String PAYMENT_EXCHANGE = "payments.exchange";
    public static final String PAYMENT_PAID_QUEUE = "notification.payment.paid.queue";
    public static final String PAYMENT_PROCESSED_ROUTING_KEY = "payments.processed";


    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue paymentPaidQueue() {
        return new Queue(PAYMENT_PAID_QUEUE, true);
    }

    @Bean
    public Binding paymentPaidBinding(Queue paymentPaidQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentPaidQueue)
                .to(paymentExchange)
                .with(PAYMENT_PROCESSED_ROUTING_KEY);
    }


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        return new RabbitAdmin(cf);
    }
    @Bean Queue notificationQueue() {
        return new Queue(CREATE_QUEUE, true);
    }

    @Bean TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean Binding bindingCreate(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(notificationExchange)
                .with(CREATE_ROUTING);
    }

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

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                         Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }

    @Bean
    @DependsOn("rabbitAdmin")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter conv
    ) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        return f;
    }
}
