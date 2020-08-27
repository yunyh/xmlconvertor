import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "my"
version = "1.0.2"

plugins {
    val kotlinVersion = "1.4.0"
    val openjfxVersion = "0.0.9"

    kotlin("jvm") version kotlinVersion
    id("org.openjfx.javafxplugin") version openjfxVersion
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    val coroutineVersion = "1.3.9"

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$coroutineVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

javafx {
    version = "14"
    modules("javafx.controls", "javafx.fxml")
}