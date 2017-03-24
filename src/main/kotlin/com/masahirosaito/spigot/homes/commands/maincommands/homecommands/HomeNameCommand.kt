package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.commands.maincommands.HomeCommand
import com.masahirosaito.spigot.homes.teleportNamedHome
import org.bukkit.entity.Player

class HomeNameCommand(homeCommand: HomeCommand) : SubCommand(homeCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_name
    )

    override fun fee(): Double = plugin.fee.HOME_NAME

    override fun configs(): List<Boolean> = listOf(
            plugin.configs.onNamedHome
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        player.teleportNamedHome(plugin, player, args[0])
    }
}
