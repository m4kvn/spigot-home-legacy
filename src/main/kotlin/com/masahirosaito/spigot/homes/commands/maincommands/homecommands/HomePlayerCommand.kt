package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.commands.maincommands.HomeCommand
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.teleportDefaultHome
import org.bukkit.entity.Player

class HomePlayerCommand(homeCommand: HomeCommand) : SubCommand(homeCommand), PlayerCommand {
    override val fee: Double = plugin.fee.HOME_PLAYER
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_player
    )

    override fun configs(): List<Boolean> = listOf(
            plugin.configs.onFriendHome
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 2 && args[0] == "-p"

    override fun execute(player: Player, args: List<String>) {
        player.teleportDefaultHome(plugin, findOfflinePlayer(args[1]))
    }
}
