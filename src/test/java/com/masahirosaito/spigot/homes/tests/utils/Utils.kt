package com.masahirosaito.spigot.homes.tests.utils

import com.masahirosaito.spigot.homes.tests.Permission
import org.bukkit.Location
import org.bukkit.entity.Player

fun Player.set(vararg permissions: Permission) {
    permissions.forEach { MockPlayerFactory.permissions[uniqueId]?.add(it.permission) }
}

fun Player.setOps() {
    MockPlayerFactory.ops.add(uniqueId)
}

fun Player.isOps() = MockPlayerFactory.ops.contains(uniqueId)
