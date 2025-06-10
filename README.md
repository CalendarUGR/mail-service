# TempusUGR - Mail Service

Este repositorio contiene el código fuente del `mail-service`, un microservicio asíncrono de la arquitectura de **TempusUGR**. Su única responsabilidad es gestionar el envío de todas las comunicaciones por correo electrónico del sistema.

Este servicio funciona de manera desacoplada, escuchando eventos de un bus de mensajes (RabbitMQ) y reaccionando a ellos para enviar correos electrónicos dinámicos y personalizados a los usuarios.

---

## ✨ Funcionalidad Principal y Flujo de Trabajo

El `mail-service` no expone una API REST para ser consumido directamente. En su lugar, opera bajo un patrón de **Productor/Consumidor**:

1.  **Escucha de Eventos**: El servicio está permanentemente conectado a **RabbitMQ**, escuchando en colas de mensajería específicas (ej. una cola para registros, otra para notificaciones de eventos).
2.  **Consumo de Mensajes**: Cuando otro microservicio (como `user-service` o `academic-subscription-service`) publica un mensaje en una de estas colas, el `mail-service` lo consume.
3.  **Generación de Contenido Dinámico**: Utiliza el motor de plantillas **Thymeleaf** para procesar plantillas HTML. El servicio inyecta en estas plantillas la información contenida en el mensaje (nombre de usuario, enlaces de activación, detalles del evento, etc.).
4.  **Envío de Correo**: Una vez generado el cuerpo del correo en HTML, utiliza **JavaMail** para conectarse a un servidor SMTP (configurado para Gmail en este proyecto) y enviar el correo al destinatario.
5.  **Fiabilidad**: La configuración incluye políticas de **reintentos automáticos** y el uso de una **Dead Letter Queue (DLQ)**. Si el envío de un correo falla repetidamente, el mensaje se mueve a la DLQ para su análisis posterior, asegurando que ninguna notificación se pierda de forma silenciosa.

![Image](https://github.com/user-attachments/assets/6a3c20e2-e46b-4475-a154-d348c3b2a4fe)

---

## 🛠️ Pila Tecnológica

* **Lenguaje/Framework**: Java 21, Spring Boot 3.4.4
* **Mensajería Asíncrona**: **Spring AMQP** con **RabbitMQ** como message broker.
* **Motor de Plantillas**: **Thymeleaf** para la generación de correos electrónicos en formato HTML.
* **Envío de Correo**: **Jakarta Mail (JavaMail)** para la comunicación con el servidor SMTP.
* **Descubrimiento de Servicios**: Cliente de **Eureka** para el registro del servicio en el ecosistema.

---

## 🏗️ Arquitectura e Integración

El `mail-service` está diseñado para ser un "trabajador" en segundo plano. Su integración principal es con RabbitMQ.

* **Productores**: `user-service` (para correos de registro, reseteo de contraseña) y `academic-subscription-service` (para notificaciones de nuevos eventos).
* **Consumidor**: Este servicio es el único consumidor de las colas de correo.

![Image](https://github.com/user-attachments/assets/bf756221-d116-4db3-8d9f-6971a69d271b)

---

## 🚀 Puesta en Marcha Local

### **Prerrequisitos**

* Java 21 o superior.
* Maven 3.x.
* Una instancia de **RabbitMQ** en ejecución.
* Un servidor **Eureka** (`eureka-service`) en ejecución para el registro.

### **Configuración**

Este servicio requiere una configuración cuidadosa de las credenciales de RabbitMQ y del servidor de correo saliente en el archivo `src/main/resources/application.properties`.

```properties
# -- CONFIGURACIÓN DEL SERVIDOR --
server.port=8084 # O el puerto deseado

# -- CONFIGURACIÓN DE EUREKA --
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka

# -- CONFIGURACIÓN DE RABBITMQ --
spring.rabbitmq.host=<rabbitmq_host>
spring.rabbitmq.port=5672
spring.rabbitmq.username=<user>
spring.rabbitmq.password=<password>

# -- CONFIGURACIÓN DE GMAIL (SMTP) --
# IMPORTANTE: Debes usar una "Contraseña de aplicación" generada en tu cuenta de Google,
# no tu contraseña de inicio de sesión normal.
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu-direccion-de-correo@gmail.com
spring.mail.password=tu-contraseña-de-aplicacion-de-16-caracteres
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
