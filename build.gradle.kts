plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("io.ktor.plugin") version "3.0.0"
}

group = "com.db"
version = "1.0.0"

application {
    mainClass.set("com.db.ApplicationKt")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
}

dependencies {
    implementation("io.ktor:ktor-server-core") // Core Ktor server dependency
    implementation("io.ktor:ktor-server-netty") // Netty engine for Ktor
    implementation("io.ktor:ktor-server-content-negotiation") // Ktor server Content negotiation
    implementation("io.ktor:ktor-client-core") // Ktor client
    implementation("io.ktor:ktor-client-cio") // CIO engine for Ktor client
    implementation("io.ktor:ktor-client-content-negotiation") // Ktor client Content negotiation
    implementation("com.google.code.gson:gson:2.10.1") // Google GSON for JSON serialization
    implementation("io.ktor:ktor-client-gson") // Ktor client GSON for JSON serialization
    implementation("io.ktor:ktor-serialization-jackson") // Jackson for JSON serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json") // Kotlinx for JSON serialization
    testImplementation("io.ktor:ktor-server-test-host") // Test host for Ktor
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0") // JUnit for testing
}

// Configure test tasks to use JUnit 5
tasks.test {
    useJUnitPlatform()
}

// Set Kotlin compiler options
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.withType<JavaCompile> {
    options.release.set(17)
}
