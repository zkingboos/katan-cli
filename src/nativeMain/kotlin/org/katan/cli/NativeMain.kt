package org.katan.cli

import kotlinx.coroutines.runBlocking

fun main(args: Array<out String>) {
    runBlocking {
        initCli(args)
    }
}