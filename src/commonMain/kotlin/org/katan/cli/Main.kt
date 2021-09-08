package org.katan.cli

import com.github.ajalt.clikt.core.subcommands
import org.katan.cli.commands.MainCommand
import org.katan.cli.commands.VersionCommand
import org.katan.cli.commands.install.InstallCommand

fun initCli(args: Array<out String>) {
    val command = MainCommand().subcommands(
        VersionCommand(),
        InstallCommand()
    )

    // avoid returning exit code 1 if no arguments provided
    if (args.isEmpty()) {
        terminal.println(command.getFormattedHelp())
        return
    }

    command.main(args)
}