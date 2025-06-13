package com.calendarugr.mail_service.service;

import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.core.io.ClassPathResource;

import com.calendarugr.mail_service.config.RabbitMQConfig;
import com.calendarugr.mail_service.config.RabbitMQErrorHandler;
import com.calendarugr.mail_service.models.Email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired 
    private TemplateEngine templateEngine;

    @SuppressWarnings("unused")
    @Autowired
    private RabbitMQErrorHandler errorHandler;

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

    @RabbitListener(queues = RabbitMQConfig.MAIL_REGISTRATION_QUEUE, errorHandler = "errorHandler")
    public void sendActivationEmail(Map<String, String> msg) throws MessagingException{
        System.out.println("📩 Recibido mensaje: " + msg);
        String email = msg.get("email");
        String token = msg.get("token");
        String type = msg.get("type");

        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true, "UTF-8");
            helper.setTo(email);

            Context context = new Context();
            context.setVariable("token", token);

            String emailContent = "";

            if (type.equals("resetPassword")){
                helper.setSubject("Reseteo de contraseña");
                emailContent = templateEngine.process("resetPassword", context);
            }else{
                helper.setSubject("Activación de cuenta TempusUGR");
                emailContent = templateEngine.process("activation", context);
            }
    
            helper.setText(emailContent, true);
            javaMailSender.send(message);
        }catch(MessagingException e){
            System.err.println("Error al enviar el correo: " + e.getMessage());
            throw new MessagingException("Error while sending email", e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.MAIL_NOTIFICATION_QUEUE, errorHandler = "errorHandler")
    public void sendNotificationEmail(Map<String, Object> msg) throws MessagingException{
        
        System.out.println("📩 Recibido mensaje: " + msg);
        List<String> emails = (List<String>) msg.get("emails");

        if (emails == null || emails.isEmpty()) {
            System.out.println("No hay destinatarios para el correo.");
            return;
        }
        
        String gradeName = (String) msg.get("gradeName");
        String subjectName = (String) msg.get("subjectName");
        String groupName = (String) msg.get("groupName");
        String date = (String) msg.get("date");
        String initHour = (String) msg.get("initHour");
        String finishHour = (String) msg.get("finishHour");
        String classroom = (String) msg.get("classroom");
        String title = (String) msg.get("title");
        String type = (String) msg.get("type");
        String facultyName = (String) msg.get("facultyName");
        String teacher = (String) msg.get("teacher");
        String day = (String) msg.get("day");

        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true, "UTF-8");
            helper.setTo(emails.toArray(new String[0]));
            helper.setSubject("Notificación de creación de evento TempusUGR");
            Context context = new Context();

            context.setVariable("gradeName", gradeName);
            context.setVariable("subjectName", subjectName);
            context.setVariable("groupName", groupName);
            context.setVariable("date", date);
            context.setVariable("initHour", initHour);
            context.setVariable("finishHour", finishHour);
            context.setVariable("classroom", classroom);
            context.setVariable("title", title);
            if (type.equals("GROUP")) {
                context.setVariable("type", "Evento a nivel de grupo");
            } else {
                context.setVariable("type", "Evento a nivel de facultad");
            }
            context.setVariable("facultyName", facultyName);
            context.setVariable("teacher", teacher);
            context.setVariable("day", day);

            String emailContent = templateEngine.process("notification", context);
            helper.setText(emailContent, true);
            javaMailSender.send(message);

        }catch(MessagingException e){
            System.err.println("Error al enviar el correo: " + e.getMessage());
            throw new MessagingException("Error while sending email", e);
        }

    }

}