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

COPY --from=builder /app/build/libs/WelcomeNotificationService-*.jar app.jar
RUN chmod 755 app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/e_commerce_welcomenotification_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_RABBITMQ_HOST=rabbitmq
ENV SERVER_PORT=8087

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8087/actuator/health || exit 1

EXPOSE 8087

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Dmail.smtp.ssl.trust=*", "-jar", "app.jar"]