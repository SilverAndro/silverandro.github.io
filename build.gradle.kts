import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    java
    application
    kotlin("jvm") version "1.8.22"
}

project.archivesName.set("website_gen")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.0")
}

application {
    mainClass.set("dev.silverandro.website.MainKt")
}

tasks.register("assembleSite", JavaExec::class) {
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("dev.silverandro.website.MainKt")
}