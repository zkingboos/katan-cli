package org.katan.cli.io

expect suspend fun executeCommand(
    command: List<String>,
    builder: CommandBuilder.() -> Unit
): String

class CommandBuilder {
    var directory: String = "./"
    var abortOnError: Boolean = false
    var redirectStdErr: Boolean = false
    var trim: Boolean = false
}