package com.masahirosaito.spigot.homes.testutils

import com.masahirosaito.spigot.homes.testutils.MockPlayerFactory
import com.masahirosaito.spigot.homes.testutils.MockWorldFactory
import com.masahirosaito.spigot.homes.testutils.SpyLogger
import org.bukkit.entity.Player
import java.io.File

fun Player.set(vararg permissions: Permission) {
    permissions.forEach { MockPlayerFactory.permissions[uniqueId]?.add(it.permission) }
}

fun Player.setOps(boolean: Boolean = true) {
    MockPlayerFactory.ops.put(uniqueId, boolean)
}

val Player.logger: SpyLogger get() = MockPlayerFactory.loggers[uniqueId]!!

fun Player.lastMsg() = logger.logs.lastOrNull()

fun Player.remove(vararg permissions: Permission) {
    permissions.forEach { MockPlayerFactory.permissions[uniqueId]?.remove(it.permission) }
}

fun Player.randomTeleport() {
    teleport(MockWorldFactory.makeRandomLocation())
}

object FileUtil {
    fun delete(file: File) {
        if (!file.exists()) return
        if (file.isDirectory) file.listFiles().forEach { FileUtil.delete(it) }
        file.delete()
    }
}
