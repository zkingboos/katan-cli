package org.katan.cli

import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.subcommands
import org.katan.cli.commands.MainCommand
import org.katan.cli.commands.VersionCommand

suspend fun initCli(args: Array<String>) {
    val terminal = Terminal()

    try {
        MainCommand().subcommands(VersionCommand()).parse(args)
    } catch (e: PrintHelpMessage) {
        println(e.command.getFormattedHelp())
    }
}