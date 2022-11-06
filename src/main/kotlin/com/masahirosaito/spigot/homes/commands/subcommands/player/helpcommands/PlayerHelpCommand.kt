package com.masahirosaito.spigot.homes.commands.subcommands.player.helpcommands

import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.Permission.home_command_help
import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.DESCRIPTION
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.USAGE_HELP
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.USAGE_HELP_COMMAND
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.createPlayerCommandListMessage
import org.bukkit.entity.Player

class PlayerHelpCommand : PlayerCommand {
    override var payNow: Boolean = true
    override val name: String = "help"
    override val description: String = DESCRIPTION
    override val permissions: List<String> = listOf(home_command_help)
    override val usage: CommandUsage = CommandUsage(this, listOf(
        "/home help" to USAGE_HELP,
        "/home help <command_name>" to USAGE_HELP_COMMAND
    ))
    override val commands: List<BaseCommand> = listOf(
            PlayerHelpUsageCommand(this)
    )

    override fun fee(): Double = homes.fee.HELP

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(player: Player, args: List<String>) {
        send(player, createPlayerCommandListMessage(player))
    }
}
