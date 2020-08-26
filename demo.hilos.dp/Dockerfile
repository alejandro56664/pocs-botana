FROM openjdk:8-jre-alpine
MAINTAINER Botana <jheison.morales@telefonica.com>

RUN apk add tzdata
RUN cp /usr/share/zoneinfo/America/Bogota /etc/localtime
RUN echo "America/Bogota" >  /etc/timezone
RUN apk del tzdata

WORKDIR /app
COPY target/demo.hilos.dp-0.0.1-SNAPSHOT.jar /app
COPY src/main/resources/application.properties config/
ENTRYPOINT java -jar demo.hilos.dp-0.0.1-SNAPSHOT.jar --spring.config.location=config/application.properties
EXPOSE 8080
