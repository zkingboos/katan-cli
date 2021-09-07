package org.katan.cli.io

import java.io.*

@Suppress("BlockingMethodInNonBlockingContext")
actual suspend fun executeCommand(
    command: List<String>,
    builder: CommandBuilder.() -> Unit
): String {
    val options = CommandBuilder().apply(builder)
    val processBuilder = ProcessBuilder()
        .command(command.filter { it.isNotBlank() })
        .directory(File(options.directory))

    val process = processBuilder.start()
    val stdout = process.inputStream.bufferedReader().use { it.readText() }
    val stderr = process.errorStream.bufferedReader().use { it.readText() }
    val exitCode = process.waitFor()
    if (options.abortOnError)
        assert(exitCode == 0)

    val output = if (stderr.isBlank())
        stdout
    else
        "$stdout $stderr"

    return if (options.trim) output.trim() else output
}