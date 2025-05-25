package com.calendarugr.mail_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitMQConfig {
    public static final String MAIL_REGISTRATION_QUEUE = "registering_queue";
    public static final String MAIL_NOTIFICATION_QUEUE = "notification_queue";
    public static final String MAIL_EXCHANGE = "mail_exchange";
    public static final String MAIL_REGISTRATION_ROUTING_KEY = "registering_routing_key";
    public static final String MAIL_NOTIFICATION_ROUTING_KEY = "notification_routing_key";

    public static final String MAIL_DEAD_LETTER_QUEUE = "mail_dead_letter_queue";
    public static final String MAIL_DEAD_LETTER_EXCHANGE = "mail_dead_letter_exchange";
    public static final String MAIL_DEAD_LETTER_ROUTING_KEY = "mail_dead_letter_routing_key";

    // Configuration of RabbitMQ listener container factory with retry capabilities

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(retryInterceptor()); // Adding retry interceptor
        factory.setDefaultRequeueRejected(false); // Not requeuing messages by default
        return factory;
    }

    @Bean
    public RetryOperationsInterceptor retryInterceptor() {
        return RetryInterceptorBuilder.stateless()
            .maxAttempts(3) // Retries up to 3 times
            .backOffOptions(1000, 2.0, 50000) // initialInterval, multiplier, maxInterval
            .recoverer(new RejectAndDontRequeueRecoverer()) // Messages will not be requeued after retries
            .build();
    }

    // Configuration of the main queues and exchange

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

    // Configuration of Dead Letter Exchange (DLX) and Dead Letter Queue (DLQ) 
    @Bean
    Queue deadLetterQueue() {
        return new Queue(MAIL_DEAD_LETTER_QUEUE, true);
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(MAIL_DEAD_LETTER_EXCHANGE);
    }

    @Bean
    Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(MAIL_DEAD_LETTER_ROUTING_KEY);
    }
}