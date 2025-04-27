FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x gradlew && \
    ./gradlew clean build --no-daemon -x test

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/NotificationService-*.jar app.jar
RUN chmod 755 app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/e_commerce_notification_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_RABBITMQ_HOST=localhost
ENV SPRING_JPA_HIBERNATE_DDL-AUTO=update

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8084/actuator/health || exit 1

EXPOSE 8084

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]