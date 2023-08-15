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



FROM openjdk:17-slim as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src


FROM base as development
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=postgre", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]


FROM base as build
RUN ./mvnw package


FROM openjdk:17-slim as production
EXPOSE 8080
COPY --from=build /app/target/jitflix-*.jar /jitflix.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/jitflix.jar"]


##CMD ["./mvnw", "spring-boot:run","-Dspring-boot.run.profiles=postgre"]

