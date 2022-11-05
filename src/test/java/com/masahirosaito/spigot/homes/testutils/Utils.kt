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
    MockPlayerFactory.ops[uniqueId] = boolean
}

val Player.logger: SpyLogger get() = MockPlayerFactory.loggers[uniqueId]!!

fun Player.lastMsg() = logger.logs.lastOrNull()

fun Player.randomTeleport() {
    teleport(MockWorldFactory.makeRandomLocation())
}

object FileUtil {
    fun delete(file: File) {
        if (!file.exists()) return
        if (file.isDirectory) file.listFiles()?.forEach { delete(it) }
        file.delete()
    }
}

fun Player.acceptInvitation() {
    if (this.hasMetadata("homes.invite")) {
        val th = (this.getMetadata("homes.invite").first().value() as Thread)
        if (th.isAlive) {
            th.interrupt()
            th.join()
        }
    }
}

fun Player.cancelTeleport() {
    getDelayThread()?.run {
        if (isAlive) {
            interrupt()
            join()
        }
    }
}

fun Player.getDelayThread(): Thread? =
        if (this.hasMetadata("homes.delay")) {
            this.getMetadata("homes.delay").first().value() as Thread
        } else null


fun CommandSender.executeHomeCommand(vararg args: String?): Boolean {
    return command.onCommand(this, pluginCommand, "home", args)
}

fun HomesConsoleCommandSender.lastMsg() = spyLogger.logs.lastOrNull()
