package org.katan.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class MainCommand : CliktCommand(
    name = "katan",
) {

    private val version: Boolean by option("-v").flag(default = false)

    override fun run() {
        if (version) {
            VersionCommand().run()
            return
        }

        echo("Katan-CLI")
        echo("https://github.com/KatanPanel/katan-cli")
    }

}