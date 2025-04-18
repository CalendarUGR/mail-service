package com.calendarugr.mail_service.config;

import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component("errorHandler")
public class RabbitMQErrorHandler implements RabbitListenerErrorHandler {

    @Override
    public Object handleError(org.springframework.amqp.core.Message amqpMessage, Channel channel, Message<?> message,
            ListenerExecutionFailedException exception) throws Exception {
        System.out.println("❌ Error procesando mensaje, será descartado: " + message);
        System.out.println("Causa del error: " + exception.getMessage());
        // Simplemente descarta el mensaje devolviendo null o lanzando una excepción controlada
        return null;
    }
}
