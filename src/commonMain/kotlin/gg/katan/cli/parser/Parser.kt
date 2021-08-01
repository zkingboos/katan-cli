package gg.katan.cli.parser

import com.jakewharton.mosaic.MosaicScope
import gg.katan.cli.framework.Command
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

/**
 * Command line arguments parser.
 *
 * @author Natan Vieira
 */
class Parser {

    companion object {

        const val MAIN_COMMAND = "katan-cli"

    }

    suspend inline fun loop(scope: MosaicScope, provider: (String) -> Command?) {
        while (true) {
            val input = readLine()?.trim() ?: continue
            val args = input.split(" ")
            if (args.size == 1 || args.first() != MAIN_COMMAND) {
                help()
                continue
            }

            val command = args[1]
            provider(command)?.also { scope.exec(it) } ?: notFound(command)
        }
    }

    suspend fun help() {
        // TODO: not implemented yet
        println("use: katan-cli [...args]")
    }

    suspend fun notFound(input: String) {
        // TODO: not implemented yet
        println("command \"$input\" not found")
    }

    // TODO: handle cancellation
    // TODO: move this to command executor class
    suspend fun MosaicScope.exec(command: Command) = coroutineScope {
        setContent {
            command.renderFunction.invoke()
        }

        command.executionFunction?.invoke(this@exec)
        // TODO: should back to the previous thread when execution ends
    }

}