FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN ./mvnw package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]