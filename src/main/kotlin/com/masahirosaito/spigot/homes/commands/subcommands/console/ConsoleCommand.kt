package com.masahirosaito.spigot.homes.commands.subcommands.console

import com.masahirosaito.spigot.homes.commands.BaseCommand
import org.bukkit.command.ConsoleCommandSender

interface ConsoleCommand : BaseCommand {

    fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>)

    fun onCommand(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        checkConfig()
        checkArgs(args)
        execute(consoleCommandSender, args)
    }
}
