plugins {
    java
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
}

group = "io.github.zapolyarnydev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.avro:avro:1.11.4")
    implementation("org.apache.kafka:kafka-clients:3.5.0")
}

avro {
    fieldVisibility.set("PRIVATE")
}

tasks.test {
    useJUnitPlatform()
}