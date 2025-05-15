# ----------------------------
# 1. Build stage
# ----------------------------
FROM gradle:8-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradle.* /app/
COPY gradle /app/gradle
RUN gradle dependencies --warning-mode=all && \
    gradle clean

COPY src /app/src
RUN gradle bootJar --warning-mode=all -x test

# ----------------------------
# 2. Run stage
# ----------------------------
FROM eclipse-temurin:17-jdk-jammy AS runtime

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
