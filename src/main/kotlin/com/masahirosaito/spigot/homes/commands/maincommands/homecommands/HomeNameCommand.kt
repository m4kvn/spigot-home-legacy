package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import org.bukkit.entity.Player

class HomeNameCommand(val homeCommand: HomeCommand) : SubCommand(homeCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_name
    )

    override fun fee(): Double = plugin.fee.HOME_NAME

    override fun configs(): List<Boolean> = listOf(
            Configs.onNamedHome
    )

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        player.teleport(homeCommand.getTeleportLocation(player, args[0]))
    }
}
