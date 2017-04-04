package com.masahirosaito.spigot.homes.commands.subcommands.player.helpcommands

import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command_help
import com.masahirosaito.spigot.homes.Permission.home_command_help_command
import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand.Companion.homeCommand
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.exceptions.NoSuchCommandException
import org.bukkit.entity.Player

class PlayerHelpUsageCommand(playerHelpCommand: PlayerHelpCommand) : SubCommand(playerHelpCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            home_command_help,
            home_command_help_command
    )

    override fun fee(): Double = homes.fee.HELP_USAGE

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        homeCommand.let {
            if (it.name == args[0] && it is PlayerCommand && it.hasPermission(player)) {
                return send(player, it.usage)
            } else {
                it.playerSubCommands.find { it.name == args[0] }.let {
                    if (it != null && it is PlayerCommand && it.hasPermission(player)) {
                        return send(player, it.usage)
                    } else throw NoSuchCommandException(args[0])
                }
            }
        }
    }
}
