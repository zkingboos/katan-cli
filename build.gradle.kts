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
    mainClassName = "$group.Main"
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
    jvm()

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
        val jvmMain by getting { dependsOn(commonMain) }
        val mingwX64Main by getting { dependsOn(nativeMain) }
    }
}

interface Injected {
    @get:Inject
    val exec: ExecOperations
    @get:Inject
    val fs: FileSystemOperations
}

val mainCommand = "katan"

tasks {
    register("completions") {
        group = "run"
        description = "Generate Bash/Zsh/Fish completion files"
        dependsOn("install")

        val injected = project.objects.newInstance<Injected>()
        val shells = listOf(
            Triple("bash", file("completions/$mainCommand.bash"), "/usr/local/etc/bash_completion.d"),
            Triple("zsh", file("completions/_$mainCommand.zsh"), "/usr/local/share/zsh/site-functions"),
            Triple("fish", file("completions/$mainCommand.fish"), "/usr/local/share/fish/vendor_completions.d"),
        )
        for ((SHELL, FILE, INSTALL) in shells) {
            actions.add {
                injected.exec.exec {
                    commandLine(mainCommand, "--generate-completion", SHELL)
                    standardOutput = FILE.outputStream()
                }

                injected.fs.copy {
                    from(FILE)
                    into(INSTALL)
                }
            }
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