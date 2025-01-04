plugins {
    kotlin("jvm")
}

group = "me.cerasi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://releases.aspose.com/java/repo/")
}

dependencies {
    implementation("com.github.auties00:cobalt:0.0.7")
    annotationProcessor("com.github.auties00:cobalt:0.0.7")
    implementation("com.github.pengrad:java-telegram-bot-api:7.9.1")

    implementation(rootProject)
}

kotlin {
    jvmToolchain(21)
}