package com.calendarugr.mail_service.service;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.calendarugr.mail_service.config.RabbitMQConfig;
import com.calendarugr.mail_service.models.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired 
    private TemplateEngine templateEngine;

    public void sendMail(Email email) throws MessagingException{

        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true, "UTF-8");
            helper.setTo(email.getReceiver());
            helper.setSubject(email.getSubject());
            //helper.setText(emailDto.getMessage(), true); Send text plain email
    
            Context context = new Context();
            context.setVariable("message", email.getMessage());
            context.setVariable("subject", email.getSubject());
            String emailContent = templateEngine.process("email", context);
    
            helper.setText(emailContent, true);
            javaMailSender.send(message);
        }catch(MessagingException e){
            throw new MessagingException("Error while sending email");
        }
        
    }

    @RabbitListener(queues = RabbitMQConfig.MAIL_QUEUE)
    public void sendActivationEmail(Map<String, String> msg) throws MessagingException{
        System.out.println("📩 Recibido mensaje: " + msg);
        String email = msg.get("email");
        String token = msg.get("token");

        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Activación de cuenta UGRCalendar");
    
            Context context = new Context();
            context.setVariable("token", token);
            String emailContent = templateEngine.process("activation", context);
    
            helper.setText(emailContent, true);
            javaMailSender.send(message);
        }catch(MessagingException e){
            throw new MessagingException("Error while sending email");
        }


    }
}