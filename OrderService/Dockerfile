# ----------------------------

FROM gradle:8-jdk17 AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradle.* /app/
COPY gradle /app/gradle
RUN gradle dependencies --warning-mode=all && gradle clean

COPY src /app/src
RUN gradle bootJar --warning-mode=all -x test

# ----

FROM eclipse-temurin:17-jdk-jammy AS runtime

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar bff-service.jar

EXPOSE 8083

ENTRYPOINT ["/srcsh", "-c", "java $JAVA_OPTS -jar bff-service.jar"]