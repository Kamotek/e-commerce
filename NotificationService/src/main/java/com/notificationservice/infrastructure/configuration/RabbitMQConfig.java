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

@EnableRabbit   // <- konieczne
@Configuration
public class RabbitMQConfig {
    // exchange + queue dla powiadomień (na przyszłość)
    public static final String EXCHANGE       = "notifications.exchange";
    public static final String CREATE_QUEUE   = "notifications.create.queue";
    public static final String CREATE_ROUTING = "notifications.create";

    // exchange + queue do obsługi eventów z OrderService
    public static final String ORDER_EXCHANGE = "orders.exchange";
    public static final String ORDER_QUEUE    = "notification.orders.create.queue";
    public static final String ORDER_KEY      = "orders.create";


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory cf) {
        return new RabbitAdmin(cf);
    }
    // 1) kolejka na powiadomienia (nieużywana w listenerze orderów)
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

    // 2) exchange/queue binding dla OrderService eventów
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

    // 3) wspólny JSON converter
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    // 4) RabbitTemplate z konwerterem
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                         Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }

    // 5) fabryka listenerów z JSON converterem
    @Bean
    @DependsOn("rabbitAdmin")    // <- listener wystartuje po adminie
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
