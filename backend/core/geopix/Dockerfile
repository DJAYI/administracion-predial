# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and pom first for dependency caching
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Make Maven wrapper executable and download dependencies (cached layer)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application, skip tests (they run in CI)
RUN ./mvnw package -DskipTests -B && \
    mv target/*.jar target/app.jar

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

# Security: run as non-root
RUN addgroup -S geopix && adduser -S geopix -G geopix

WORKDIR /app

# Copy only the fat JAR from build stage
COPY --from=build /app/target/app.jar app.jar

# Switch to non-root user
USER geopix

EXPOSE 8080

# JVM tuning for containers
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
