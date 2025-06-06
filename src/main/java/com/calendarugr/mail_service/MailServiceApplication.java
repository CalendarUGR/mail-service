package com.calendarugr.mail_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableDiscoveryClient
public class MailServiceApplication {

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
		System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
		System.setProperty("API_KEY", dotenv.get("API_KEY"));
		System.setProperty("EUREKA_URL", dotenv.get("EUREKA_URL"));
		System.setProperty("RABBITMQ_HOST", dotenv.get("RABBITMQ_HOST"));
		System.setProperty("RABBITMQ_PORT", dotenv.get("RABBITMQ_PORT"));
		System.setProperty("RABBITMQ_USERNAME", dotenv.get("RABBITMQ_USERNAME"));
		System.setProperty("RABBITMQ_PASSWORD", dotenv.get("RABBITMQ_PASSWORD"));
		SpringApplication.run(MailServiceApplication.class, args);
	}

}
