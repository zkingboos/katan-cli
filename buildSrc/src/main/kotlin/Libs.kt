object Libs {

    const val kotlinVersion = "1.5.10"

    const val clikt = "com.github.ajalt.clikt:clikt:3.2.0"
    const val mordant = "com.github.ajalt.mordant:mordant:2.0.0-beta2"
    const val katanSdk = "org.katan:katan-sdk:0.0.1"

    object Ktor {
        private const val prefix = "io.ktor:ktor"
        const val version = "1.6.3"

        const val client = "$prefix-client-core:$version"
    }

    object KTX {
        object Coroutines {
            private const val prefix = "org.jetbrains.kotlinx:kotlinx-coroutines"
            const val version = "1.5.0-native-mt"

            const val core = "$prefix-core:$version"
        }
    }

}