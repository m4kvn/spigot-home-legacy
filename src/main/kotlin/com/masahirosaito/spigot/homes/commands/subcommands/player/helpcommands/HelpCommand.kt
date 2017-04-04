package com.masahirosaito.spigot.homes.commands.subcommands.player.helpcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.Permission.home_command_help
import com.masahirosaito.spigot.homes.commands.*
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.commands.maincommands.MainCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.DESCRIPTION
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.USAGE_HELP
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.USAGE_HELP_COMMAND
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class HelpCommand(val mainCommand: MainCommand) : PlayerCommand, ConsoleCommand {
    override val homes: Homes = mainCommand.homes
    override val name: String = "help"
    override val description: String = DESCRIPTION()
    override val permissions: List<String> = listOf(home_command_help)
    override val consoleCommandUsage: CommandUsage = CommandUsage(this, listOf(
            "/home help" to USAGE_HELP(),
            "/home help <command_name>" to USAGE_HELP_COMMAND()
    ))
    override val playerCommandUsage: CommandUsage = CommandUsage(this, listOf(
            "/home help" to USAGE_HELP(),
            "/home help <command_name>" to USAGE_HELP_COMMAND()
    ))
    override val commands: List<BaseCommand> = listOf(
            HelpUsageCommand(this)
    )

    override fun fee(): Double = homes.fee.HELP

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        send(player, buildString {
            append("&6Homes command list&r\n")
            append("&d/home help <command_name> : ${USAGE_HELP_COMMAND()}&r\n")
            mainCommand.playerSubCommands.forEach {
                if (it is PlayerCommand && it.hasPermission(player))
                    append("  &b${it.name}&r : ${it.description}\n")
            }
            if (mainCommand is PlayerCommand && mainCommand.hasPermission(player))
                append("  &b${mainCommand.name}&r : ${mainCommand.description}\n")
        })
    }

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        send(consoleCommandSender, buildString {
            append("&6Homes command list&r\n")
            append("&d/home help <command_name> : ${USAGE_HELP_COMMAND()}&r\n")
            mainCommand.playerSubCommands.forEach {
                if (it is ConsoleCommand)
                    append("  &b${it.name}&r : ${it.description}\n")
            }
            if (mainCommand is ConsoleCommand)
                append("  &b${mainCommand.name}&r : ${mainCommand.description}\n")
        })
    }
}
