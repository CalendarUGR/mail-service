package com.calendarugr.mail_service.config;

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
    public static final String MAIL_REGISTRATION_QUEUE = "registering_queue";
    public static final String MAIL_NOTIFICATION_QUEUE = "notification_queue";
    public static final String MAIL_EXCHANGE = "mail_exchange";
    public static final String MAIL_REGISTRATION_ROUTING_KEY = "registering_routing_key";
    public static final String MAIL_NOTIFICATION_ROUTING_KEY = "notification_routing_key";

    @Bean
    Queue registrationQueue() {
        return new Queue(MAIL_REGISTRATION_QUEUE, true);
    }

    @Bean
    Queue notificationQueue() {
        return new Queue(MAIL_NOTIFICATION_QUEUE, true);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(MAIL_EXCHANGE);
    }

    @Bean
    Binding registrationBinding(Queue registrationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(registrationQueue).to(exchange).with(MAIL_REGISTRATION_ROUTING_KEY);
    }

    @Bean
    Binding notificationBinding(Queue notificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(notificationQueue).to(exchange).with(MAIL_NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}