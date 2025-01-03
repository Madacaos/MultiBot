plugins {
    kotlin("jvm") version "2.0.20"
}

group = "me.cerasi"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://releases.aspose.com/java/repo/")
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("com.github.auties00:cobalt:0.0.7")
    annotationProcessor("com.github.auties00:cobalt:0.0.7")
    implementation("com.github.pengrad:java-telegram-bot-api:7.9.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}