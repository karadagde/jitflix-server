## Start with a base image containing Java runtime
#FROM openjdk:17-jdk-alpine
#
## The application's jar file
#ARG JAR_FILE=target/*.jar
#
## Add the application's jar to the container
#COPY ${JAR_FILE} app.jar
#
## Run the jar file
#ENTRYPOINT ["java","-jar","/app.jar"]



FROM openjdk:17-slim

WORKDIR /app

COPY target/Jitflix-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]