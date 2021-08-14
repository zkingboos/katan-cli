package org.katan.cli

import kotlinx.coroutines.runBlocking

actual val platform: Platform by lazy {
    val os = System.getProperty("os.name").lowercase()
    when {
        os.startsWith("windows") -> Platform.WINDOWS
        os.startsWith("linux") -> Platform.LINUX
        os.startsWith("mac") -> Platform.MACOS
        os.startsWith("darwin") -> Platform.MACOS
        else -> error("Unknown platform: $os")
    }
}

fun main(args: Array<String>) {
    runBlocking {
        initCli(args)
    }
}