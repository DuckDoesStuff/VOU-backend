FROM gradle:jdk21-alpine as builder
WORKDIR /app
COPY . .
RUN gradle --no-daemon build -x test

FROM openjdk:21
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "app.jar"]