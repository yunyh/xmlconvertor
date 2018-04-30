import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.HasConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.js.translate.context.Namer.kotlin

version = "1.0"

buildscript {
    var kotlinVersion: String by extra
    kotlinVersion = "1.2.41"

    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlinVersion))
    }
}

apply {
    plugin("kotlin")
    plugin("maven")
}

val kotlinVersion: String by extra

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlinVersion))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

/*
val sourceJar = task<Jar>("sourceJar") {
    classifier = "sources"
    from(the<JavaPluginConvention>().sourceSets.getByName("main").allSource)
}

artifacts.add("archives", sourceJar)
*/

/*
application {
    mainClassName = "MainKt"
}

tasks.withType<Jar>{
    baseName = "MyApp"
    version = "0.0.1"

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }

    manifest {
        attributes["Main-Class"] = "MainKt"
    }
}
*/

/*
val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.runtime.map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks["jar"] as CopySpec)
}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}
*/

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "XmlConverter"
    manifest {
        attributes["Implementation-Title"] = "My App"
        attributes["Implementation-Version"] = version
        attributes["Main-Class"] = "MainKt"
    }
    from(configurations.runtime.map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks["jar"] as CopySpec)
}

tasks {
    "jar" {
        dependsOn(fatJar)
    }
}