package com.authservice.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String USER_REG_QUEUE   = "auth.user.register.queue";
    public static final String USER_REG_EXCHANGE = "auth.user.exchange";
    public static final String USER_REG_ROUTING  = "auth.user.register";

    public static final String USER_LOGIN_QUEUE    = "auth.user.login.queue";
    public static final String USER_LOGIN_EXCHANGE = USER_REG_EXCHANGE;
    public static final String USER_LOGIN_ROUTING  = "auth.user.login";

    @Bean
    public Queue userRegistrationQueue() {
        return new Queue(USER_REG_QUEUE, true);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_REG_EXCHANGE);
    }

    @Bean
    public Binding bindingUserRegistration(Queue userRegistrationQueue, TopicExchange userExchange) {
        return BindingBuilder
                .bind(userRegistrationQueue)
                .to(userExchange)
                .with(USER_REG_ROUTING);
    }

    @Bean
    public Queue userLoginQueue() {
        return new Queue(USER_LOGIN_QUEUE, true);
    }

    @Bean
    public Binding bindingUserLogin(Queue userLoginQueue, TopicExchange userExchange) {
        return BindingBuilder
                .bind(userLoginQueue)
                .to(userExchange)
                .with(USER_LOGIN_ROUTING);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}