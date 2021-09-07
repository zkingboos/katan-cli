package org.katan.cli.io

actual suspend fun executeCommand(
    command: List<String>,
    builder: CommandBuilder.() -> Unit
): String {
    throw UnsupportedOperationException("Not supported")
}