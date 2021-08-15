package org.katan.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import org.katan.cli.printVersion

class VersionCommand : CliktCommand(
    name = "version",
) {

    override fun run() {
        printVersion()
    }

}