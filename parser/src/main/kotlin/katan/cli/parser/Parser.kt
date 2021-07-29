package katan.cli.parser

import com.jakewharton.mosaic.MosaicScope
import katan.cli.framework.Command
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

/**
 * Command line arguments parser.
 *
 * @author Natan Vieira
 */
class Parser {

    suspend inline fun loop(scope: MosaicScope, provider: (String) -> Command?) {
        while (true) {
            val input = readLine()?.trim() ?: continue
            provider(input)?.also { scope.exec(it) } ?: notFound(input)
        }
    }

    suspend fun notFound(input: String) {
        // TODO: not implemented yet
    }

    // TODO: handle cancellation
    // TODO: move this to command executor class
    suspend fun MosaicScope.exec(command: Command) = coroutineScope {
        setContent {
            command.renderFunction.invoke()
        }

        command.executionFunction.invoke()

        // TODO: should back to the previous thread when execution ends
    }

}