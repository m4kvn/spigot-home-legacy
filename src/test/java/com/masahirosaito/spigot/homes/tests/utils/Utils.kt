package com.masahirosaito.spigot.homes.tests.utils

import com.masahirosaito.spigot.homes.tests.Permission
import org.bukkit.Location
import org.bukkit.entity.Player

fun Player.set(permission: Permission) {
    MockPlayerFactory.permissions[uniqueId]?.add(permission.permission)
}

fun Player.setOps() {
    MockPlayerFactory.ops.add(uniqueId)
}

fun Player.isOps() = MockPlayerFactory.ops.contains(uniqueId)
