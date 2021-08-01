import io.github.gciatto.kt.node.*

plugins {
    id("com.github.johnrengelman.shadow") version "6.0.0"
    id("io.github.gciatto.kt-npm-publish") version "0.3.9"
    kotlin("multiplatform") version Libs.kotlinVersion
    kotlin("plugin.serialization") version Libs.kotlinVersion
    application
}

val PROJECT = "katan-cli"

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
    }
}

npmPublishing {
    val organization = "katan.gg"

    token.set(System.getenv("NPM_AUTH_TOKEN"))
    liftPackageJson {
        main = PROJECT
        homepage = "https://github.com/KatanPanel/katan-cli"
        bugs = Bugs("https://github.com/KatanPanel/katan-cli/issues")
        license = "MIT"
        bins = mutableMapOf(PROJECT to "./$PROJECT")
        keywords = mutableListOf("katan", "cli", "kotlin", "multiplatform")
        files = mutableListOf("bin/$PROJECT")
        name = "@$organization/$name"
        dependencies = dependencies?.mapKeys { (key, _) ->
            if (name!! in key) "@$organization/$key" else key
        }?.toMutableMap()
    }

    liftJsSources { _, _, line ->
        line.replace("'$PROJECT", "'@$organization/$PROJECT")
            .replace("\"$PROJECT", "\"@$organization/$PROJECT")
    }
}
