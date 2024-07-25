plugins {
    java
    application
    kotlin("jvm") version "2.0.0"
}

kotlin {
    jvmToolchain(17)

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.4.0")

    implementation("org.ow2.asm:asm:9.5")
    implementation("org.ow2.asm:asm-tree:9.5")
}

application {
    mainClass.set("dev.silverandro.website.MainKt")
}

tasks.register("assembleSite", JavaExec::class) {
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("dev.silverandro.website.MainKt")
}