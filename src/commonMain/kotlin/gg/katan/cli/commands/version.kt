package gg.katan.cli.commands

import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import gg.katan.cli.framework.command

val Version = command("version") {
    render {
        Column {
            Text("KatanCLI v0.0.0")
        }
    }
}