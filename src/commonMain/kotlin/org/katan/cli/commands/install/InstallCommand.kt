package org.katan.cli.commands.install

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import org.katan.cli.commands.install.ui.InstallUICommand

class InstallCommand : NoOpCliktCommand(
    name = "install",
    printHelpOnEmptyArgs = true
) {

    init {
        subcommands(InstallUICommand())
    }

}