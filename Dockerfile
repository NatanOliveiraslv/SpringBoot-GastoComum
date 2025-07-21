FROM ubuntu:latest AS builder

RUN apt-get update
RUN apt-get install openjdk-22-jdk -y
COOPY..

RUN apt-get install maven -y
RUN mvn clean install

FROM eclipse-temurin:22-jdk

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
