package org.katan.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import org.katan.cli.printVersion

class MainCommand : CliktCommand(
    name = "katan",
) {

    private val version: Boolean by option("-v").flag(default = false)

    override fun run() {
        if (version)
            return printVersion()

        echo("https://github.com/KatanPanel/katan-cli")
    }

}