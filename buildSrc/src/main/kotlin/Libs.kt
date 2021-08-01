object Libs {

    const val kotlinVersion = "1.5.20"

    object KTX {
        object Coroutines {
            private const val prefix = "org.jetbrains.kotlinx:kotlinx-coroutines"
            private const val version = "1.5.1"

            const val core = "$prefix-core:$version"
        }
    }

}