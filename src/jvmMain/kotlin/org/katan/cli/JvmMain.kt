package org.katan.cli

import com.github.ajalt.mordant.animation.progressAnimation
import com.github.ajalt.mordant.markdown.Markdown
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.runBlocking

actual val platform: Platform by lazy {
    val os = System.getProperty("os.name").lowercase()
    when {
        os.startsWith("windows") -> Platform.WINDOWS
        os.startsWith("linux") -> Platform.LINUX
        os.startsWith("mac") -> Platform.MACOS
        os.startsWith("darwin") -> Platform.MACOS
        else -> error("Unknown platform: $os")
    }
}

fun main(args: Array<out String>) {
    runBlocking {
        initCli(args)

        val terminal = Terminal()

        // Detect the terminal size so our progress bar is as wide as the screen
        terminal.info.updateTerminalSize()

        val progress = terminal.progressAnimation {
            text("my-file.bin")
            percentage()
            progressBar()
            completed()
            speed("B/s")
            timeRemaining()
        }

        progress.start()

        // Sleep for a few seconds to show the indeterminate state
        Thread.sleep(5000)

        // Update the progress as the download progresses
        progress.updateTotal(3_000_000_000)
        repeat(200) {
            progress.advance(15_000_000)
            Thread.sleep(100)
        }

        progress.stop()
    }
}