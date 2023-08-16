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



# FROM openjdk:17-slim as base
# WORKDIR /app
# COPY .mvn/ .mvn
# COPY mvnw pom.xml ./
# RUN ./mvnw dependency:resolve
# COPY src ./src

# FROM base as test
# RUN ["./mvnw", "test"]

# FROM base as development
# CMD ["./mvnw", "spring-boot:run", "-Dspring.profiles.active=development", "-Dspring.config.location=file:/app/src/main/resources/", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]


# FROM base as build
# RUN ./mvnw package


# FROM openjdk:17-slim as production
# EXPOSE 8080
# COPY --from=build /app/target/jitflix-*.jar /jitflix.jar
# CMD ["java", "-Dspring.profiles.active=production", "-Dspring.config.location=file:/app/src/main/resources/", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/jitflix.jar"]


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

FROM base as compile
RUN ./mvnw compile

FROM compile as test
RUN ["./mvnw", "test", "-Dspring.profiles.active=test", "-Dspring.config.location=file:/app/src/main/resources/"]

FROM test as build
RUN ./mvnw package

FROM openjdk:17-slim as production
EXPOSE 8080
COPY --from=build /app/target/jitflix-*.jar /jitflix.jar
CMD ["java", "-Dspring.profiles.active=production", "-Dspring.config.location=file:/app/src/main/resources/", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/jitflix.jar"]


##CMD ["./mvnw", "spring-boot:run","-Dspring-boot.run.profiles=postgre"]

