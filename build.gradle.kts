plugins {
    id("java")
}

group = "io.github.zapolyarnydev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

configure(listOf(project(":auth-service"))) {
    tasks.register<Test>("unitTest") {
        description = "Runs unit tests"
        group = "verification"

        testClassesDirs = sourceSets.test.get().output.classesDirs
        classpath = sourceSets.test.get().runtimeClasspath

        useJUnitPlatform {
            includeTags("unit")
        }
    }
}

tasks.register("unitTestAll") {
    group = "verification"
    description = "Runs unit tests in all subprojects"
    dependsOn(
        listOf(project(":auth-service"))
            .map { it.tasks.named("unitTest") }
    )
}


tasks.test {
    useJUnitPlatform()
}