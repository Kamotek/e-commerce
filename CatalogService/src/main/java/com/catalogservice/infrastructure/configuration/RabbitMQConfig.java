package com.catalogservice.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME            = "catalog.exchange";
    public static final String CREATE_QUEUE_NAME        = "catalog.create.queue";
    public static final String UPDATE_QUEUE_NAME        = "catalog.update.queue";
    public static final String CREATE_ROUTING_KEY       = "catalog.create";
    public static final String UPDATE_ROUTING_KEY       = "catalog.update";

    public static final String READ_QUEUE_NAME    = "catalog.read.queue";
    public static final String READ_ROUTING_KEY   = "catalog.read";


    @Bean
    public Queue createQueue() {
        return new Queue(CREATE_QUEUE_NAME, true);
    }

    @Bean
    public Queue updateQueue() {
        return new Queue(UPDATE_QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange catalogExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding bindingCreate(Queue createQueue, TopicExchange catalogExchange) {
        return BindingBuilder.bind(createQueue)
                .to(catalogExchange)
                .with(CREATE_ROUTING_KEY);
    }

    @Bean
    public Binding bindingUpdate(Queue updateQueue, TopicExchange catalogExchange) {
        return BindingBuilder.bind(updateQueue)
                .to(catalogExchange)
                .with(UPDATE_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
    }

    @Bean
    public Queue readQueue() {
        return new Queue(READ_QUEUE_NAME, true);
    }

    @Bean
    public Binding bindingRead(Queue readQueue, TopicExchange catalogExchange) {
        return BindingBuilder.bind(readQueue)
                .to(catalogExchange)
                .with(READ_ROUTING_KEY);
    }

}
