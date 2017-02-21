package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.entity.Player

interface SubCommand {
    val plugin: Homes
    val name: String
    val permission: String
    val usage: String

    fun execute(player: Player, args: List<String>): Boolean

    fun hasPermission(player: Player): Boolean {
        return if (permission.isNullOrBlank()) true else player.hasPermission(permission)
    }
}