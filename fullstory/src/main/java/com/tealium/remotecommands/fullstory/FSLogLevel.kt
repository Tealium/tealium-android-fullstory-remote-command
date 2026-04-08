package com.tealium.remotecommands.fullstory

import com.fullstory.FS
import java.util.Locale

internal fun String.toFSLogLevel(): FS.LogLevel? = when (lowercase(Locale.ROOT)) {
    "log",     "fslog_log"     -> FS.LogLevel.LOG
    "error",   "fslog_error"   -> FS.LogLevel.ERROR
    "warn", "warning", "fslog_warning" -> FS.LogLevel.WARN
    "info",    "fslog_info"    -> FS.LogLevel.INFO
    "debug",   "fslog_debug"   -> FS.LogLevel.DEBUG
    else -> null
}
