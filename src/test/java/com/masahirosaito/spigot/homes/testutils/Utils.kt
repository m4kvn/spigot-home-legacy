package com.masahirosaito.spigot.homes.testutils

import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.command
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.pluginCommand
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.spyLogger
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

fun Player.setPermissions(vararg permissions: String) {
    permissions.forEach { MockPlayerFactory.permissions[uniqueId]?.add(it) }
}

fun Player.setOps(boolean: Boolean = true) {
    MockPlayerFactory.ops.put(uniqueId, boolean)
}

val Player.logger: SpyLogger get() = MockPlayerFactory.loggers[uniqueId]!!

fun Player.lastMsg() = logger.logs.lastOrNull()

fun Player.removePermissions(vararg permissions: String) {
    permissions.forEach { MockPlayerFactory.permissions[uniqueId]?.remove(it) }
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

fun Player.acceptInvitation() {
    if (this.hasMetadata("homes.invite")) {
        val th = (this.getMetadata("homes.invite")[0].value() as Thread)
        if (th.isAlive) {
            th.interrupt()
            th.join()
        }
    }
}

fun CommandSender.executeHomeCommand(vararg args: String?) {
    command.onCommand(this, pluginCommand, "home", args)
}

fun HomesConsoleCommandSender.lastMsg() = spyLogger.logs.lastOrNull()
