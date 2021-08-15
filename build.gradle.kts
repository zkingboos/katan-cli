plugins {
    kotlin("multiplatform") version Libs.kotlinVersion
    kotlin("plugin.serialization") version Libs.kotlinVersion
    `maven-publish`
}

group = "org.katan.cli"
version = "0.0.1"

repositories {
    mavenCentral()
}

// setup common binary executable entrypoint to native targets
fun org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests.entryPoint() {
    binaries {
        executable {
            entryPoint = "$group.main"
        }
    }
}

kotlin {
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
                implementation(Libs.mordant)
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val linuxX64Main by getting { dependsOn(nativeMain) }
        val macosX64Main by getting { dependsOn(nativeMain) }
        val mingwX64Main by getting { dependsOn(nativeMain) }
    }
}

tasks {
    val os = System.getProperty("os.name")
    val nativeTarget = when {
        os == "Mac OS X" -> "MacosX64"
        os == "Linux" -> "LinuxX64"
        os.startsWith("Windows") -> "MingwX64"
        else -> throw GradleException("OS $os is not supported in Kotlin/Native")
    }


    register<Copy>("install") {
        group = "run"
        description = "Build the native executable and install in the current platform"
        dependsOn("runDebugExecutable$nativeTarget")

        val commandName = "katan"
        val nativeTargetName = nativeTarget.first().toLowerCase() + nativeTarget.substring(1)
        val sourceDirectory = "build/bin/$nativeTargetName/debugExecutable"
        from(sourceDirectory) {
            include("${rootProject.name}.kexe")
            rename { commandName }
        }

        val targetDir = "/usr/local/bin"
        into(targetDir)
        doLast {
            println("$ cp $sourceDirectory/${rootProject.name}.kexe $targetDir/$commandName")
        }
    }
}