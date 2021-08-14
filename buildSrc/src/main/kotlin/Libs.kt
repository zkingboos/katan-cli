object Libs {

    const val kotlinVersion = "1.5.20"

    const val clikt = "com.github.ajalt.clikt:clikt:3.2.0"
    const val mordant = "com.github.ajalt.mordant:mordant:2.0.0-beta2"

    object KTX {
        object Coroutines {
            private const val prefix = "org.jetbrains.kotlinx:kotlinx-coroutines"
            private const val version = "1.5.1"

            const val core = "$prefix-core:$version"
        }
    }

}