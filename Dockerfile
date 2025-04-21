FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
EXPOSE 8082
COPY ./target/mail-service-0.0.1-SNAPSHOT.jar mail-service.jar
COPY .env .env

ENTRYPOINT ["java", "-jar", "mail-service.jar"]