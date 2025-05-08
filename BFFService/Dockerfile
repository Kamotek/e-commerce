# ----------------------------
# 1. Build stage
# ----------------------------
FROM gradle:8-jdk17 AS builder

WORKDIR /app

# 1.1. Cache zależności
COPY build.gradle settings.gradle gradle.* /app/
COPY gradle /app/gradle
RUN gradle dependencies --warning-mode=all && gradle clean

# 1.2. Kompilacja
COPY src /app/src
RUN gradle bootJar --warning-mode=all -x test

# ----------------------------
# 2. Run stage
# ----------------------------
FROM eclipse-temurin:17-jdk-jammy AS runtime

WORKDIR /app

# kopiujemy fat-jar
COPY --from=builder /app/build/libs/*.jar bff-service.jar

# expose port
EXPOSE 8085

# entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar bff-service.jar"]