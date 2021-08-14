plugins {
    id("com.github.johnrengelman.shadow") version "6.0.0"
    kotlin("multiplatform") version Libs.kotlinVersion
    kotlin("plugin.serialization") version Libs.kotlinVersion
    application
    `maven-publish`
}

group = "org.katan.cli"
version = "0.0.1"

repositories {
    mavenCentral()
}

application {
    mainClass.set("$group.JvmMainKt")
}

// setup common binary executable entrypoint to native targets
fun org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests.entryPoint() {
    binaries {
        executable {
            entryPoint = "$group.main"
        }
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    val jvmTarget = jvm() {

    }
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
                implementation(Libs.clikt)
                // implementation(Libs.mordant)
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

        shadowJar {
            from(jvmTarget.compilations.getByName("main").output)
            configurations = mutableListOf(
                jvmTarget.compilations.getByName("main").compileDependencyFiles as Configuration,
                jvmTarget.compilations.getByName("main").runtimeDependencyFiles as Configuration
            )
        }
    }
}

tasks {
    register("allRun") {
        group = "run"
        description = "Run on the JVM and native executables"

        val os = System.getProperty("os.name")
        val nativeTarget = when {
            os == "Mac OS X" -> "MacosX64"
            os == "Linux" -> "LinuxX64"
            os.startsWith("Windows") -> "MingwX64"
            else -> throw GradleException("OS $os is not supported in Kotlin/Native")
        }

        dependsOn("run", "runDebugExecutable$nativeTarget")
    }

    register("ci") {
        group = "run"
        description = "Run all tests and run native executables"
        dependsOn("allTests", "allRun")
    }
}