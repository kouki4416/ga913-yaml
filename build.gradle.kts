plugins {
    kotlin("jvm") version libs.versions.kotlin
    application
    alias(libs.plugins.ktlint)
    alias(libs.plugins.serialization)
}

group = "com.tfandkusu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kaml)
    implementation(libs.kotlin.poet)
    implementation(libs.swift.poet)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
}

tasks.test {
    useJUnit()
}
kotlin {
    jvmToolchain(17)
}
application {
    mainClass.set("com.tfandkusu.ga913yaml.MainKt")
}
