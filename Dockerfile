# Build stage
FROM gradle:8.9-jdk21 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test --no-daemon --refresh-dependencies

# Run stage (FIX)
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]