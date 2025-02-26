# Mail Service - CalendarUGR

## Descripción
El **Mail Service** es un microservicio dentro del sistema **CalendarUGR** encargado del envío de correos electrónicos. Actualmente, su función principal es completar el registro de usuarios mediante el envío de emails, pero en el futuro también servirá para notificar cambios de horario.

## 🚀 Tecnologías utilizadas

- **Spring Boot**
- **Java Mail** para el envío de correos electrónicos
- **Thymeleaf** para la generación de plantillas de correo
- **RabbitMQ** para la mensajería asíncrona

## 📌 Funcionalidades

- Recibe mensajes a través de **RabbitMQ**
- Envía correos electrónicos para completar el registro de usuarios
- Usa **Thymeleaf** para plantillas de email
- En el futuro, permitirá notificaciones de cambios de horario

## 📦 Requisitos previos
Para ejecutar este servicio, es necesario configurar las variables de entorno del "main"