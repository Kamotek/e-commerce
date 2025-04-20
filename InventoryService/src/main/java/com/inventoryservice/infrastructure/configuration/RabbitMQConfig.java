package com.inventoryservice.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Queue;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "inventory.exchange";
    public static final String ADD_QUEUE = "inventory.add.queue";
    public static final String REMOVE_QUEUE = "inventory.remove.queue";
    public static final String ADD_KEY = "inventory.add";
    public static final String REMOVE_KEY = "inventory.remove";

    @Bean
    TopicExchange exchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Jackson2JsonMessageConverter converter(ObjectMapper m){
        return new Jackson2JsonMessageConverter(m);
    }
    @Bean
    RabbitTemplate tpl(ConnectionFactory cf, Jackson2JsonMessageConverter c){
        RabbitTemplate t=new RabbitTemplate(cf);
        t.setMessageConverter(c);
        return t;
    }
    @Bean
    SimpleRabbitListenerContainerFactory factory(ConnectionFactory cf, Jackson2JsonMessageConverter c){
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(c);
        return f;
    }
}