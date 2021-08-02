import io.github.gciatto.kt.node.*

plugins {
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("io.github.gciatto.kt-npm-publish") version "0.3.9"
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

// setup common binary executable entrypoint to some targets
fun org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests.entryPoint() {
    binaries {
        executable {
            entryPoint = "main"
        }
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvm()
    macosX64 { entryPoint() }
    mingwX64 { entryPoint() }
    linuxX64 { entryPoint() }

    js(LEGACY) {
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

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        val linuxX64Main by getting {
            dependsOn(nativeMain)
        }

        val mingwX64Main by getting {
            dependsOn(nativeMain)
        }

        val macosX64Main by getting {
            dependsOn(nativeMain)
        }

        val jsMain by getting {
            repositories {
                jcenter()
            }

            dependencies {
                implementation(Libs.KTX.nodeJs)
            }
        }
    }
}

npmPublishing {
    val pkg = "cli"
    val organization = "katan.gg"

    liftPackageJson {
        main = PROJECT
        homepage = "https://github.com/KatanPanel/katan-cli"
        bugs = Bugs("https://github.com/KatanPanel/katan-cli/issues")
        license = "MIT"
        bins = mutableMapOf(PROJECT to "./$PROJECT")
        keywords = mutableListOf("katan", "cli", "kotlin", "multiplatform")
        files = mutableListOf("bin/$PROJECT")
        name = "@$organization/$pkg"
        dependencies = dependencies?.mapKeys { (key, _) ->
            if (name!! in key) "@$organization/$key" else key
        }?.toMutableMap()
    }

    liftJsSources { _, _, line ->
        line.replace("'$PROJECT", "'@$organization/$pkg")
            .replace("\"$PROJECT", "\"@$organization/$pkg")
    }

    token.set(System.getenv("NPM_AUTH_TOKEN"))
}
