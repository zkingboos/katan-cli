plugins {
    kotlin("multiplatform") version Libs.kotlinVersion
    kotlin("plugin.serialization") version Libs.kotlinVersion
    id("application")
}

group = "org.katan"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

application {
    mainClassName = "$group.cli.MainKt"
}

// setup common binary executable entrypoint to native targets
fun org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests.entryPoint() {
    binaries {
        executable {
            entryPoint = "$group.cli.main"
        }
    }
}

kotlin {
    macosX64 { entryPoint() }
    mingwX64 { entryPoint() }
    linuxX64 { entryPoint() }
    jvm()

    sourceSets {
        all {
            languageSettings {
                useExperimentalAnnotation("kotlin.RequiresOptIn")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(Libs.KTX.Coroutines.core) {
                    version { strictly(Libs.KTX.Coroutines.version) }
                }
                implementation(Libs.katanSdk)
                implementation(Libs.clikt)
                implementation(Libs.mordant)
                implementation(Libs.Ktor.client)
            }
        }

        val jvmMain by getting { dependsOn(commonMain) }
        val nativeMain by creating { dependsOn(commonMain) }
        val posixMain by creating { dependsOn(nativeMain) }
        val mingwX64Main by getting { dependsOn(nativeMain) }
        val linuxX64Main by getting {
            dependsOn(posixMain)
            dependsOn(nativeMain)
        }
        val macosX64Main by getting {
            dependsOn(posixMain)
            dependsOn(nativeMain)
        }
    }
}

val mainCommand = "katan"

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
    }

    register<Copy>("install") {
        group = "run"
        description = "Build the native executable and install in the current platform"

        val os = System.getProperty("os.name")
        val nativeTarget = when {
            os == "Mac OS X" -> "MacosX64"
            os == "Linux" -> "LinuxX64"
            os.startsWith("Windows") -> "MingwX64"
            else -> throw GradleException("OS $os is not supported in Kotlin/Native")
        }

        dependsOn("runDebugExecutable$nativeTarget")

        val nativeTargetName = nativeTarget.first().toLowerCase() + nativeTarget.substring(1)
        val sourceDirectory = "build/bin/$nativeTargetName/debugExecutable"
        from(sourceDirectory) {
            include("${rootProject.name}.kexe")
            rename { mainCommand }
        }

        val targetDir = "/usr/local/bin"
        into(targetDir)
        doLast {
            println("$ cp $sourceDirectory/${rootProject.name}.kexe $targetDir/$mainCommand")
        }
    }
}