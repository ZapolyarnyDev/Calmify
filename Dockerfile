FROM gradle:8.12.0-jdk21 AS builder

WORKDIR /build

ARG SERVICE_MODULE

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
RUN gradle :${SERVICE_MODULE}:dependencies --no-daemon

COPY calmify-commons calmify-commons
COPY ${SERVICE_MODULE}/build.gradle.kts ${SERVICE_MODULE}/
COPY ${SERVICE_MODULE}/src ${SERVICE_MODULE}/src

RUN gradle :${SERVICE_MODULE}:bootJar --no-daemon --parallel -x test

FROM eclipse-temurin:21-jre

ARG SERVICE_MODULE
ARG VERSION
LABEL version="${VERSION}"
ARG EXPOSE_PORT

ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

WORKDIR /app
COPY --from=builder /build/${SERVICE_MODULE}/build/libs/app.jar app.jar

EXPOSE ${EXPOSE_PORT}

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
