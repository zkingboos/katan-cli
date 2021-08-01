plugins {
    kotlin("multiplatform") version Libs.kotlinVersion
    kotlin("plugin.serialization") version Libs.kotlinVersion
    id("com.github.johnrengelman.shadow") version "6.0.0"
    application
}

group = "gg.katan.cli"

repositories {
    mavenCentral()
}

application {
    mainClass.set("gg.katan.cli.JvmMainKt")
}

// setup common binary executable entrypoint to some targets
fun org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests.entryPoint() {
    binaries {
        executable {
            entryPoint = "main"
        }
    }
}

kotlin {
    jvm()
    macosX64 { entryPoint() }
    mingwX64 { entryPoint() }
    linuxX64 { entryPoint() }

    js {
        nodejs()
        binaries.executable()
    }

    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }

        val commonMain by getting {
            dependencies {
                implementation(Libs.KTX.Coroutines.core)
            }
        }
    }
}