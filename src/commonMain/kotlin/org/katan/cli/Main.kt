package org.katan.cli

import com.github.ajalt.clikt.core.*
import com.github.ajalt.mordant.terminal.*
import org.katan.cli.commands.*

val terminal = Terminal()

fun initCli(args: Array<out String>) {
    val argv = arrayOf("version", "--help")
    val command = MainCommand().subcommands(VersionCommand())

    // avoid returning exit code 1 if no arguments provided
    if (argv.isEmpty()) {
        terminal.println(command.getFormattedHelp())
        return
    }

    command.main(argv)
}