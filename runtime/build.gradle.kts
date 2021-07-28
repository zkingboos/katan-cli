plugins {
    id("com.github.johnrengelman.shadow") version "6.0.0"
    application
}

application {
    mainClassName = "katan.cli.runtime.MainKt"
}

dependencies {
    implementation(project(":framework"))
    implementation(project(":commands"))
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = application.mainClassName
            attributes["Implementation-Version"] = project.version
        }
    }

    shadowJar {
        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }
}