package gg.katan.cli

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

actual val platform: Platform by lazy {
    // NodeJS Platform API
    when(os.platform()) {
        "win32" -> Platform.WINDOWS
        "darwin" -> Platform.MACOS
        else -> Platform.LINUX
    }
}

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@JsExport
@JsName("katancli")
fun main(args: Array<String>) {
    GlobalScope.promise {
        initCli(args)
    }
}