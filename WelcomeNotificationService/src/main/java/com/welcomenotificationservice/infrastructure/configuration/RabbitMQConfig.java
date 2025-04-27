package com.welcomenotificationservice.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
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
    public static final String USER_REG_EXCHANGE   = "auth.user.exchange";
    public static final String USER_REG_ROUTING    = "auth.user.register";
    public static final String WELCOME_QUEUE       = "welcome.notification.user.register.queue";

    @Bean TopicExchange userExchange() {
        return new TopicExchange(USER_REG_EXCHANGE);
    }

    @Bean Queue welcomeQueue() {
        return new Queue(WELCOME_QUEUE, true);
    }

    @Bean Binding bindingWelcome() {
        return BindingBuilder
                .bind(welcomeQueue())
                .to(userExchange())
                .with(USER_REG_ROUTING);
    }

    @Bean Jackson2JsonMessageConverter messageConverter(ObjectMapper om) {
        return new Jackson2JsonMessageConverter(om);
    }

    @Bean RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                        Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }

    @Bean SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter conv
    ) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        return f;
    }
}
