package org.katan.cli

enum class Platform {
    MACOS,
    LINUX,
    WINDOWS
}

expect val platform: Platform