FROM openjdk:8-jdk-alpine
MAINTAINER smallintro.github.com
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring/
RUN mkdir -p /home/spring/logs
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} email-service-1.0.0.jar
ENTRYPOINT ["java","-jar","email-service-1.0.0.jar"]
