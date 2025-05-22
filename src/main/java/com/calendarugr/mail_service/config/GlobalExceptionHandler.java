package com.calendarugr.mail_service.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.calendarugr.mail_service.dtos.ErrorResponseDTO;

import jakarta.mail.MessagingException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponseDTO> handleMessagingException(MessagingException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponseDTO("MessagingException", e.getMessage()));
    }
}