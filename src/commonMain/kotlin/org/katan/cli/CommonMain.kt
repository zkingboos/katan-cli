package org.katan.cli

import com.github.ajalt.clikt.core.subcommands
import org.katan.cli.commands.MainCommand
import org.katan.cli.commands.VersionCommand

suspend fun initCli(args: Array<out String>) {
    println("Initialized @ $platform")
    MainCommand().subcommands(VersionCommand()).main(args)
}