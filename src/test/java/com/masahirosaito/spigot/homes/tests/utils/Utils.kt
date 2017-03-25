package com.masahirosaito.spigot.homes.tests.utils

import com.masahirosaito.spigot.homes.tests.Permission
import org.bukkit.Location
import org.bukkit.entity.Player

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
