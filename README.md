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

## Requisitos previos
Para ejecutar este servicio, es necesario configurar las siguientes variables de entorno:

- `MAIL_USERNAME`: Correo que usarás.
- `MAIL_PASSWORD`: Contraseña de aplicación del servicio de mails que uses.

## Instalación y ejecución
1. Clonar el repositorio:
   ```sh
   git clone <repository-url>
   cd mail-service

2. Configurar variables de entorno 

    ```sh
    export MAIL_USERNAME=<your_mail_username>
    export MAIL_PASSWORD=<your_mail_password>

3. Construir y ejecutar el servicio

    ```sh
    ./mvnw spring-boot:run