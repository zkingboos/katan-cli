package gg.katan.cli

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

@DelicateCoroutinesApi
@ExperimentalJsExport
@JsExport
@JsName("katancli")
fun main(args: Array<String>) {
    GlobalScope.promise {
        initCli(args)
    }
}