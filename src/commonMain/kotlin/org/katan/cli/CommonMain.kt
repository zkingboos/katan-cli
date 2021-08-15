package org.katan.cli

import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.mordant.rendering.TextColors.Companion.rgb
import com.github.ajalt.mordant.terminal.Terminal
import org.katan.cli.commands.MainCommand
import org.katan.cli.commands.VersionCommand

const val PRIMARY_COLOR = "#4b7bec"

val terminal = Terminal()

internal fun printVersion() {
    terminal.println(rgb(PRIMARY_COLOR)("Katan-CLI v0.0.1 @ $platform"))
}

suspend fun initCli(args: Array<String>) {
    val command = MainCommand().subcommands(VersionCommand())

    // avoid returning exit code 1 if no arguments provided
    if (args.isEmpty()) {
        terminal.println(command.getFormattedHelp())
        return
    }

    command.main(args)
}