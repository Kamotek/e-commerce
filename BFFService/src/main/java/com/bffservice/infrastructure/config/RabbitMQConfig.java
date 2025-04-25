package com.bffservice.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Binding;
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
    public static final String EXCHANGE = "auth.user.exchange";
    public static final String CMD_REG = "auth.user.register";
    public static final String CMD_LOGIN = "auth.user.login";
    public static final String EVT_REG = "user.registered";
    public static final String EVT_LOGIN = "user.loggedin";

    @Bean Queue cmdRegisterQueue() { return new Queue("bff.cmd.register", true); }
    @Bean Queue cmdLoginQueue()    { return new Queue("bff.cmd.login", true); }
    @Bean Queue evtRegisterQueue() { return new Queue("bff.evt.registered", true); }
    @Bean Queue evtLoginQueue()    { return new Queue("bff.evt.loggedin",  true); }
    @Bean TopicExchange exchange() { return new TopicExchange(EXCHANGE); }

    @Bean Binding bindCmdReg(Queue cmdRegisterQueue, TopicExchange ex) {
        return BindingBuilder.bind(cmdRegisterQueue).to(ex).with(CMD_REG);
    }
    @Bean Binding bindCmdLogin(Queue cmdLoginQueue, TopicExchange ex) {
        return BindingBuilder.bind(cmdLoginQueue).to(ex).with(CMD_LOGIN);
    }
    @Bean Binding bindEvtReg(Queue evtRegisterQueue, TopicExchange ex) {
        return BindingBuilder.bind(evtRegisterQueue).to(ex).with(EVT_REG);
    }
    @Bean Binding bindEvtLogin(Queue evtLoginQueue, TopicExchange ex) {
        return BindingBuilder.bind(evtLoginQueue).to(ex).with(EVT_LOGIN);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper om) {
        return new Jackson2JsonMessageConverter(om);
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf,
                                         Jackson2JsonMessageConverter conv) {
        RabbitTemplate tpl = new RabbitTemplate(cf);
        tpl.setMessageConverter(conv);
        return tpl;
    }
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter conv
    ) {
        var f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(conv);
        return f;
    }
}