package com.masahirosaito.spigot.homes.tests.utils

import com.masahirosaito.spigot.homes.tests.Permission
import org.bukkit.Location
import org.bukkit.entity.Player

fun Player.set(vararg permissions: Permission) {
    permissions.forEach { MockPlayerFactory.permissions[uniqueId]?.add(it.permission) }
}

fun Player.setOps(boolean: Boolean = true) {
    if (boolean) MockPlayerFactory.ops.add(uniqueId)
    else MockPlayerFactory.ops.remove(uniqueId)
}

fun Player.isOps() = MockPlayerFactory.ops.contains(uniqueId)

val Player.logger: SpyLogger get() = MockPlayerFactory.loggers[uniqueId]!!

fun Player.lastMsg() = logger.logs.last()

fun Player.remove(vararg permissions: Permission) {
    permissions.forEach { MockPlayerFactory.permissions[uniqueId]?.remove(it.permission) }
}
