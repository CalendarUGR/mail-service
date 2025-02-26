package com.calendarugr.mail_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calendarugr.mail_service.models.Email;
import com.calendarugr.mail_service.service.EmailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Email email) throws MessagingException{
        try{
            emailService.sendMail(email);
            return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
        }catch(MessagingException e){
            throw new MessagingException("Error while sending email");
        }
    }

}
