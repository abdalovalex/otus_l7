FROM maven:3.8.7-eclipse-temurin-17-alpine as build
WORKDIR /app

COPY pom.xml .

RUN mvn --batch-mode --fail-never dependency:resolve-plugins dependency:resolve --file pom.xml

COPY src src

RUN mvn clean install -DskipTests
RUN cp /app/target/stock-service*.jar /app/target/stock-service.jar

FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/stock-service.jar /app/stock-service.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/app/stock-service.jar"]
