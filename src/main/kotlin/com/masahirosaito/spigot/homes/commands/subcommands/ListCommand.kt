package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ListCommand(override val plugin: Homes) : SubCommand {
    override val name  = "list"
    override val permission = Permission.home_command_list
    override var resultMessage = ""
    override val usage = buildString {
        append("${ChatColor.GOLD}List Command Usage:\n")
        append("${ChatColor.BLUE}/home list${ChatColor.RESET} : Display the list of set homes\n")
        append("${ChatColor.BLUE}/home list <player_name>${ChatColor.RESET} : Display the list of player's set homes")
    }

    override fun execute(player: Player, args: List<String>) {

    }
}