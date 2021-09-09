package org.katan.cli.commands

import com.github.ajalt.clikt.core.*

class MainCommand : NoOpCliktCommand(
    name = "katan",
    printHelpOnEmptyArgs = true
)