package katan.cli.runtime

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
    var count by mutableStateOf(0)

    render {
        Text("Count: $count", color = Color.Black, background = Color.Yellow)
    }

    // NOTE: this is in the main thread
    execution {
        while (true) {
            delay(150)
            count++
        }
    }
}

fun main() {
    System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)

    val registry = Registry(Test)
    val lock = Job()
    val parser = Parser()

    Runtime.getRuntime().addShutdownHook(Thread {
        lock.complete()
    })

    runMosaic {
        // launch as unconfined to ensure post-yielding
        // of the last frame and next subsequent command execution
        withContext(Unconfined) {
            parser.loop(this@runMosaic) { registry.search(it) }
        }

        // ensure that Mosaic's scope never go away
        lock.join()
    }
}