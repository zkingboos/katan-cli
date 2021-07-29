package katan.cli.framework

import androidx.compose.runtime.Composable

typealias RenderFn = @Composable () -> Unit
typealias ExecFn = suspend () -> Unit

interface Command {

    val name: String

    val renderFunction: RenderFn

    val executionFunction: ExecFn

}

private class CommandImpl(
    override val name: String,
    override val renderFunction: RenderFn,
    override val executionFunction: ExecFn
) : Command

class CommandBuilder(private val commandName: String) {

    private var _renderFn: RenderFn? = null
    private var _execFn: ExecFn? = null

    fun render(block: RenderFn) {
        _renderFn = block
    }

    fun execution(block: ExecFn) {
        _execFn = block
    }

    fun build(): Command {
        if (_renderFn == null) error("Render function must be defined")
        if (_execFn == null) error("Execution function must be defined")

        return CommandImpl(commandName, _renderFn!!, _execFn!!)
    }

}

inline fun command(name: String, builder: CommandBuilder.() -> Unit): Command {
    return CommandBuilder(name).apply(builder).build()
}