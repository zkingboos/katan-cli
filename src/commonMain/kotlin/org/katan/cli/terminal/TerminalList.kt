package org.katan.cli.terminal

import com.github.ajalt.mordant.terminal.Terminal

class ListWidget(
    private val terminal: Terminal,
    private val items: Map<String, String>,
    private val factory: (item: Map.Entry<String, String>, index: Int, focused: Boolean, checked: Boolean) -> String
) {

    private var lineCount = 0
    private var index = 0
    private var checks = mutableSetOf<Int>()

    internal fun render() {
        items.onEachIndexed { currIndex, item ->
            val focused = index == currIndex
            val checked = currIndex in checks
            val lines = factory(item, index, focused, checked).split("\n")
            lineCount += lines.size

            val prefix = buildString {
                append(if(focused) "-" else  "")
                append("[")
                append(if(checked) "*" else " ")
                append("]")
                append(" ")
            }

            println(prefix + lines.first())
            lines.slice(0..1).forEach { line ->
                println("${" ".repeat(prefix.length)}$line")
            }
        }
    }

    fun clear() {
        terminal.cursor.move {
            for (i in 0..lineCount) {
                up(1)
                clearLine()
            }
        }
    }

    fun set(index: Int) {
        checks.add(index)
        refresh()
    }

    fun refresh() {
        clear()
        render()
    }

}

fun Terminal.list(
    items: Map<String, String>,
    factory: (
        item: Map.Entry<String, String>,
        index: Int,
        focused: Boolean,
        checked: Boolean
    ) -> String = { item, _, _, _ -> item.value }
): ListWidget = ListWidget(this, items, factory).also { it.render() }