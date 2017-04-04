package com.masahirosaito.spigot.homes.commands.subcommands.console.listcommands

import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.DESCRIPTION_CONSOLE_COMMAND
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.PLAYER_LIST
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.USAGE_CONSOLE_COMMAND_LIST
import com.masahirosaito.spigot.homes.strings.commands.ListCommandStrings.USAGE_LIST_PLAYER
import org.bukkit.command.ConsoleCommandSender

class ConsoleListCommand : ConsoleCommand {
    override val name: String = "list"
    override val description: String = DESCRIPTION_CONSOLE_COMMAND()
    override val commands: List<BaseCommand> = listOf(
            ConsoleListPlayerCommand(this)
    )
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "/home list" to USAGE_CONSOLE_COMMAND_LIST(),
            "/home list <player_name>" to USAGE_LIST_PLAYER()
    ))

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        send(consoleCommandSender, PLAYER_LIST())
    }

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()
}
