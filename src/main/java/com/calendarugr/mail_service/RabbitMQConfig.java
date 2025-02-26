package com.calendarugr.mail_service;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String MAIL_QUEUE = "mail_queue";
    public static final String MAIL_EXCHANGE = "mail_exchange";
    public static final String MAIL_ROUTING_KEY = "mail_routing_key";

    @Bean
    Queue queue() {
        return new Queue(MAIL_QUEUE, true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(MAIL_EXCHANGE);
    }

    @Bean
    Binding binding(Queue mailQueue, DirectExchange exchange) {
        return BindingBuilder.bind(mailQueue).to(exchange).with(MAIL_ROUTING_KEY);
    }

    @Bean
    MessageConverter messageConverter() {
        // Jackson2JsonMessageConverter will be used for JSON-to-Map conversion
        return new Jackson2JsonMessageConverter();
    }
}
