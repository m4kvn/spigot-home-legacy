package com.masahirosaito.spigot.homes.utils

import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger


class SpyLogger(logger: Logger) : Logger(logger.name, logger.resourceBundleName) {

    val logs: MutableList<String> = mutableListOf()

    override fun info(msg: String?) {
        logs.add(msg!!)
        super.log(Level.INFO, msg)
    }

    override fun log(level: Level, message: String, t: Throwable) {
        log(LogRecord(level, message).apply { thrown = t })
    }
}
