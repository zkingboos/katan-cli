import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.jakewharton.mosaic:mosaic-gradle-plugin:0.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
    }
}

group = "katan.cli"
version = "1.0-SNAPSHOT"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.jakewharton.mosaic")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-cli-jvm:0.3.2")
        implementation("com.jakewharton.mosaic:mosaic-runtime:0.1.0")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
            kotlinOptions.freeCompilerArgs = listOf("-Xjvm-default=compatibility")
        }
    }
}
