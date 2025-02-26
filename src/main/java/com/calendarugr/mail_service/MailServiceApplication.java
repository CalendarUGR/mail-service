package com.calendarugr.mail_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class MailServiceApplication {

	// First Time Docker Run RabbitMQ -> docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
	// docker start rabbitmq
	// docker stop rabbitmq
	// http://localhost:15672/ -> guest:guest

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		
		SpringApplication.run(MailServiceApplication.class, args);
	}

}
