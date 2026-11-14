# =========================
# BUILD
# =========================
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml ./

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp dependency:go-offline

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -ntp package

# =========================
# RUNTIME
# =========================
FROM eclipse-temurin:25-jre

WORKDIR /app

RUN groupadd --system --gid 10001 spring && \
    useradd --system \
      --uid 10001 \
      --gid spring \
      --no-create-home spring

COPY --from=build --chown=spring:spring \
    /app/target/authentication-api-*.jar app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
