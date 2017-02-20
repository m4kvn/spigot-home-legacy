package com.masahirosaito.spigot.homes.commands.subcommands

import org.bukkit.entity.Player

interface SubCommand {
    val name: String
    val permission: String

    fun execute(player: Player, args: List<String>)

    fun hasPermission(player: Player): Boolean {
        return if (permission.isNullOrBlank()) true else player.hasPermission(permission)
    }
}