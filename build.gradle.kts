import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "katan.cli"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("com.jakewharton.mosaic") version "0.1.0"
    application
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath("com.jakewharton.mosaic:mosaic-gradle-plugin:0.1.0")
    }
}

application {
    mainClassName = "katan.cli.MainKt"
}

repositories {
    mavenCentral()
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    jar {
        manifest {
            attributes["Main-Class"] = application.mainClassName
            attributes["Implementation-Version"] = project.version
        }
    }

    build {
        dependsOn(shadowJar)
    }
}