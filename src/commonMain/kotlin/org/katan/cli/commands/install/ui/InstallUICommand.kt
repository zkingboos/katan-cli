package org.katan.cli.commands.install.ui

import com.github.ajalt.clikt.core.CliktCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.katan.cli.terminal
import org.katan.cli.terminal.list

class InstallUICommand : CliktCommand(
    name = "ui"
), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    override fun run() {
        launch {
            terminal.info("List render")
            val list = terminal.list(mapOf(
                "Test 1" to "Value of Test 1",
                "Test 2" to "Value of Test 2",
                "Test 3" to "Value of Test 3"
            ))

            delay(3000)
            list.set(1)
        }
    }

}