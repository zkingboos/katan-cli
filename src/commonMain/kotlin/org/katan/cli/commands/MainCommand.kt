package org.katan.cli.commands

import com.github.ajalt.clikt.completion.completionOption
import com.github.ajalt.clikt.core.NoOpCliktCommand

class MainCommand : NoOpCliktCommand(
    name = "katan",
) {

    init {
        completionOption()
    }

}