package org.katan.cli.commands

import com.github.ajalt.clikt.core.*
import com.github.ajalt.mordant.rendering.*
import org.katan.cli.*

class VersionCommand : CliktCommand(
    name = "version",
    help = "Show the Katan version information"
) {

    override fun run() {
        terminal.println(TextColors.rgb(PRIMARY_COLOR)("Katan-CLI v$VERSION"))
    }

}