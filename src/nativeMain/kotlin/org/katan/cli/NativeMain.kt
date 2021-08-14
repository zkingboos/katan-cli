package org.katan.cli

import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        initCli(args)
    }
}