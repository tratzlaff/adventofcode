import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // The kotlin plugin makes our project automatically compile the code in the directory src/main/kotlin.
    kotlin("jvm") version "1.9.20"

    application
}

group = "com.tratzlaff"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    // If using JUnit 5, an explicit runtimeOnly dependency on junit-platform-launcher is required
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    // Specify the Java version to be used by the Java Toolchain for compilation and execution of your code.
    // When a specific version is set, Gradle will try to find a locally installed JDK that meets the requirements.
    // If no suitable local installation is found AND download repositories have been configured,
    // then Gradle will download a matching JDK.
    // NOTE: The jvmToolchain specification should match the kotlinOptions.jvmTarget to ensure consistency between
    // the bytecode generated and the JVM used for compilation and execution.
    jvmToolchain(17)
}

tasks.withType<KotlinCompile> {
    // Instruct the Kotlin compiler to generate Java bytecode that is compatible with the specified version of the JVM.
    // NOTE: This needs to match the jvmToolchain specification.
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("MainKt")
}