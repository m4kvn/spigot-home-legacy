package com.masahirosaito.spigot.homes.commands.subcommands.console.helpcommands

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand.Companion.homeCommand
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.exceptions.NoSuchCommandException
import org.bukkit.command.ConsoleCommandSender

class ConsoleHelpUsageCommand(consoleHelpCommand: ConsoleHelpCommand) :
        SubCommand(consoleHelpCommand), ConsoleCommand {

    override fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        homeCommand.let { command ->
            command.consoleSubCommands.find { it.name == args[0] }.let {
                if (it != null) {
                    return send(consoleCommandSender, it.usage)
                } else throw NoSuchCommandException(args[0])
            }
        }
    }

    override fun configs(): List<Boolean> = listOf()

    override fun isValidArgs(args: List<String>): Boolean = args.size == 1
}
