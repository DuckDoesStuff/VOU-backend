FROM maven:3.9.8-eclipse-temurin-22-alpine as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:22
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8002
ENTRYPOINT ["java", "-jar", "app.jar"]