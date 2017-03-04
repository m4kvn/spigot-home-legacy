package com.masahirosaito.spigot.homes.tests.utils

import com.masahirosaito.spigot.homes.tests.commands.Permission
import org.bukkit.entity.Player

fun Player.set(permission: Permission) {
    MockPlayerFactory.permissions[uniqueId]?.add(permission.permission)
}
