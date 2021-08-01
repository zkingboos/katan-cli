package gg.katan.cli.runtime

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.Color.Companion.Cyan
import com.jakewharton.mosaic.Column
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.runMosaic
import gg.katan.cli.framework.Registry
import gg.katan.cli.framework.command
import gg.katan.cli.parser.Parser
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Unconfined

val Test = command("test") {
    var stage by mutableStateOf(0)

    render {
        Column {
            Text(
                when (stage) {
                    0 -> "Enter your credentials"
                    1 -> "Select database"
                    else -> "Completed!"
                }, color = Cyan
            )
        }
    }

    execute {
        println("Type your username")
        val username = withContext(Dispatchers.Default) {
            readLine()
        }

        setContent {
            Column {
                Text("Email: $username")
            }
        }
    }
}

fun main() {
    val registry = Registry(Test)
    val parser = Parser()

    runMosaic {
        // launch as unconfined to ensure post-yielding
        // of the last frame and next subsequent command execution
        withContext(Unconfined) {
            parser.loop(this@runMosaic) { registry.search(it) }
        }
    }
}