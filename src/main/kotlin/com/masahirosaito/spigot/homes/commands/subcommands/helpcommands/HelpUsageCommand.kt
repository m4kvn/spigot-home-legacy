package com.masahirosaito.spigot.homes.commands.subcommands.helpcommands

import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.commands.PlayerCommand
import com.masahirosaito.spigot.homes.commands.SubCommand
import com.masahirosaito.spigot.homes.exceptions.NoSuchCommandException
import org.bukkit.entity.Player

class HelpUsageCommand(val helpCommand: HelpCommand) : SubCommand(helpCommand), PlayerCommand {
    override val permissions: List<String> = listOf(
            Permission.home_command,
            Permission.home_command_help_command
    )

    override fun fee(): Double = homes.fee.HELP_USAGE

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1

    override fun execute(player: Player, args: List<String>) {
        helpCommand.mainCommand.subCommands.find { it.name == args[0] }?.let {
            send(player, it.usage)
        } ?: throw NoSuchCommandException(args[0])
    }
}
