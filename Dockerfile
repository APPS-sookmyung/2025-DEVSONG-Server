# Build stage
FROM gradle:8.9-jdk21 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test --no-daemon --refresh-dependencies

# Run stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]