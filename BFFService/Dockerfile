# Etap budowania
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

COPY --from=builder /app/build/libs/BFFService-*.jar app.jar
RUN chmod 755 app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8085
ENV AUTH_SERVICE_URL=http://auth-service
ENV CATALOG_SERVICE_URL=http://catalog-service
ENV ORDER_SERVICE_URL=http://order-service
ENV JWT_SECRET=default-secret-change-in-production
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/e_commerce_bff_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_RABBITMQ_HOST=rabbitmq

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8085/actuator/health || exit 1

EXPOSE 8085

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]