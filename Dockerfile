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

ARG PRIMARY_DB_URL="jdbc:h2:mem:testdb"
ARG PRIMARY_DB_DRIVER="org.h2.Driver"
ARG PRIMARY_DB_USER="sa"
ARG PRIMARY_DB_PASSWORD=""
ARG SECONDARY_DB_URL="jdbc:h2:mem:testdb"
ARG JPA_DIALECT="org.hibernate.dialect.H2Dialect"
ARG SEC_USER_PASSWORD="password"
ARG JWT_SECRET_KEY="secret"
ARG AWS_REGION="us-east-1"
ARG S3_BUCKET="bucket"

FROM openjdk:17-slim as base
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base as test
ENV SPRING_PROFILES_ACTIVE=test \
    SPRING_CONFIG_LOCATION=file:/app/src/main/resources/ \
    PRIMARY_DB_URL=${PRIMARY_DB_URL} \
    PRIMARY_DB_DRIVER=${PRIMARY_DB_DRIVER} \
    PRIMARY_DB_USER=${PRIMARY_DB_USER} \
    PRIMARY_DB_PASSWORD=${PRIMARY_DB_PASSWORD} \
    SECONDARY_DB_URL=${SECONDARY_DB_URL} \
    JPA_DIALECT=${JPA_DIALECT} \
    SEC_USER_PASSWORD=${SEC_USER_PASSWORD} \
    JWT_SECRET_KEY=${JWT_SECRET_KEY} \
    AWS_REGION=${AWS_REGION} \
    S3_BUCKET=${S3_BUCKET}

RUN ["./mvnw", "test", "-Dspring.profiles.active=test"]


FROM base as development
CMD ["./mvnw", "spring-boot:run", "-Dspring.profiles.active=development", "-Dspring.config.location=file:/app/src/main/resources/", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]


FROM base as build
RUN ./mvnw package



FROM openjdk:17-slim as production
EXPOSE 8080
#COPY --from=build /app/target/jitflix-*.jar /jitflix.jar
COPY --from=build /app/target/Jitflix-0.0.1-SNAPSHOT.jar /jitflix.jar
CMD ["java", "-Dspring.profiles.active=production", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/jitflix.jar"]


