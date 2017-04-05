package com.masahirosaito.spigot.homes.commands.subcommands.console.helpcommands

import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.CommandUsage
import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand.Companion.homeCommand
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.CONSOLE_COMMAND_LIST
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.DESCRIPTION
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.USAGE_HELP
import com.masahirosaito.spigot.homes.strings.commands.HelpCommandStrings.USAGE_HELP_COMMAND
import org.bukkit.command.ConsoleCommandSender

class ConsoleHelpCommand : ConsoleCommand {
    override val name: String = "help"
    override val description: String = DESCRIPTION()
    override val commands: List<BaseCommand> = listOf(ConsoleHelpUsageCommand(this))
    override val usage: CommandUsage = CommandUsage(this, listOf(
            "home help" to USAGE_HELP(),
            "home help <command_name>" to USAGE_HELP_COMMAND()
    ))

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.isEmpty()

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        send(consoleCommandSender, CONSOLE_COMMAND_LIST())
    }
}
