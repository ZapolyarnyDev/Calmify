plugins {
    java
    war
    id("org.springframework.boot") version "3.4.7"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "io.github.zapolyarnydev"
version = "0.1.2"

repositories {
    mavenCentral()
    maven ( url = "https://packages.confluent.io/maven/")
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation(project(":calmify-commons"))

    implementation("io.confluent:kafka-avro-serializer:8.0.0")

    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")

    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.avro:avro:1.11.4")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
}

extra["springCloudVersion"] = "2024.0.0"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.bootJar {
    archiveFileName.set("app.jar")
}

tasks.test {
    useJUnitPlatform()
}