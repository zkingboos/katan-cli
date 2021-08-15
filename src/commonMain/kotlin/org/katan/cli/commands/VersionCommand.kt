package org.katan.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.mordant.rendering.TextColors
import org.katan.cli.KATAN_VERSION
import org.katan.cli.PRIMARY_COLOR
import org.katan.cli.platform
import org.katan.cli.terminal

class VersionCommand : CliktCommand(
    name = "version",
) {

    override fun run() {
        terminal.println(TextColors.rgb(PRIMARY_COLOR)("Katan-CLI v$KATAN_VERSION @ $platform"))
    }

}