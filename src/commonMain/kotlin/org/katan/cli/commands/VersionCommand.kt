package org.katan.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import org.katan.cli.platform

class VersionCommand : CliktCommand(
    name = "version",
) {

    override fun run() {
        echo("Katan-CLI v0.0.1 @ $platform")
    }

}