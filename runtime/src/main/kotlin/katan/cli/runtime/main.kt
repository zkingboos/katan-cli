package katan.cli.runtime

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.Color
import com.jakewharton.mosaic.Text
import com.jakewharton.mosaic.runMosaic
import katan.cli.framework.Registry
import katan.cli.framework.command
import katan.cli.parser.Parser
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Unconfined

val Test = command("test") {
    var result by mutableStateOf(" ")

    render {
        Text(result, color = Color.Black, background = Color.Yellow)
    }

    // NOTE: this is in the main thread
    execution {
        val text = "Ola Rafael Bacanor"
        var index = 0
        do {
            result = text.substring(0 until (index + 1))
            delay(100)
        } while (++index != text.length)
    }
}

fun main() {
    System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)

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