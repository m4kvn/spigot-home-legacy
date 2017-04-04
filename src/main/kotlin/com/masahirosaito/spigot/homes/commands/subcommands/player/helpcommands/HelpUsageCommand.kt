package com.masahirosaito.spigot.homes.commands.subcommands.helpcommands

import com.masahirosaito.spigot.homes.Permission.home_command_help
import com.masahirosaito.spigot.homes.Permission.home_command_help_command
import com.masahirosaito.spigot.homes.commands.ConsoleCommand
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.exceptions.NoSuchCommandException
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class HelpUsageCommand(val helpCommand: HelpCommand) : SubCommand(helpCommand), PlayerCommand, ConsoleCommand {
    override val permissions: List<String> = listOf(
            home_command_help,
            home_command_help_command
    )

    override fun fee(): Double = homes.fee.HELP_USAGE

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        helpCommand.mainCommand.let {
            if (it.name == args[0] && it is PlayerCommand && it.hasPermission(player)) {
                return send(player, it.playerCommandUsage)
            } else {
                it.subCommands.find { it.name == args[0] }.let {
                    if (it != null && it is PlayerCommand && it.hasPermission(player)) {
                        return send(player, it.playerCommandUsage)
                    } else throw NoSuchCommandException(args[0])
                }
            }
        }
    }

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        helpCommand.mainCommand.let {
            if (it.name == args[0] && it is ConsoleCommand) {
                return send(consoleCommandSender, it.consoleCommandUsage)
            } else {
                it.subCommands.find { it.name == args[0] }.let {
                    if (it != null && it is ConsoleCommand) {
                        return send(consoleCommandSender, it.consoleCommandUsage)
                    } else throw NoSuchCommandException(args[0])
                }
            }
        }
    }
}
