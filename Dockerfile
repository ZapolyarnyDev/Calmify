FROM gradle:8.12.0-jdk21 AS builder

ARG SERVICE_MODULE
WORKDIR /build

COPY . .
RUN gradle :${SERVICE_MODULE}:bootJar

FROM eclipse-temurin:21-jre

ARG SERVICE_MODULE
ARG EXPOSE_PORT

ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

WORKDIR /app
COPY --from=builder /build/${SERVICE_MODULE}/build/libs/app.jar app.jar

EXPOSE ${EXPOSE_PORT}

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
