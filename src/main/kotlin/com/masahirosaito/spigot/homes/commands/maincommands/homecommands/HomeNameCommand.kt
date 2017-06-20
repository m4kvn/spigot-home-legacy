package com.masahirosaito.spigot.homes.commands.maincommands.homecommands

import com.masahirosaito.spigot.homes.Configs.onNamedHome
import com.masahirosaito.spigot.homes.DelayTeleporter
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command
import com.masahirosaito.spigot.homes.Permission.home_command_name
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import org.bukkit.entity.Player

class HomeNameCommand(val homeCommand: HomeCommand) : SubCommand(homeCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            home_command,
            home_command_name
    )

    override fun fee(): Double = homes.fee.HOME_NAME

    override fun configs(): List<Boolean> = listOf(onNamedHome)

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        DelayTeleporter.run(player, homeCommand.getTeleportLocation(player, args[0]))
    }
}
