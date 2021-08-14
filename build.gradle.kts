plugins {
    id("com.github.johnrengelman.shadow") version "6.0.0"
    kotlin("multiplatform") version Libs.kotlinVersion
    kotlin("plugin.serialization") version Libs.kotlinVersion
    application
    `maven-publish`
}

val PROJECT = "katan-cli"

group = "gg.katan.cli"
version = "0.0.1"

repositories {
    mavenCentral()
}

application {
    mainClass.set("gg.katan.cli.JvmMainKt")
}

// setup common binary executable entrypoint to native targets
fun org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests.entryPoint() {
    binaries {
        executable {
            entryPoint = "gg.katan.cli.main"
        }
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    val jvmTarget = jvm()
    macosX64 { entryPoint() }
    mingwX64 { entryPoint() }
    linuxX64 { entryPoint() }

    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
        }

        val commonMain by getting {
            dependencies {
                implementation(Libs.KTX.Coroutines.core)
                implementation("com.github.ajalt.clikt:clikt:3.2.0")
                implementation("com.github.ajalt.mordant:mordant:2.0.0-beta2")
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val linuxX64Main by getting { dependsOn(nativeMain) }
        val macosX64Main by getting { dependsOn(nativeMain) }
        val mingwX64Main by getting { dependsOn(nativeMain) }
    }

    tasks {
        withType<JavaExec> {
            val compilation = jvmTarget.compilations.getByName<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation>("main")
            classpath(files(
                compilation.runtimeDependencyFiles,
                compilation.output.allOutputs
            ))
        }
    }
}

tasks {
    register<Copy>("install") {
        group = "run"
        description = "Build and install native executable"

        val hostOs = System.getProperty("os.name")
        val nativeTarget = when {
            hostOs == "Mac OS X" -> "MacosX64"
            hostOs == "Linux" -> "LinuxX64"
            hostOs.startsWith("Windows") -> "MingwX64"
            else -> throw GradleException("Host $hostOs is not supported in Kotlin/Native.")
        }

        dependsOn("runDebugExecutable$nativeTarget")
        val targetLowercase = nativeTarget.first().toLowerCase() + nativeTarget.substring(1)
        val folder = "build/bin/$targetLowercase/debugExecutable"
        from(folder) {
            include("${rootProject.name}.kexe")
            rename { PROJECT }
        }

        val destDir = "/usr/local/bin"
        into(destDir)
        doLast {
            println("$ cp $folder/${rootProject.name}.kexe $destDir/$PROJECT")
        }
    }
}