package com.masahirosaito.spigot.homes.commands

import org.bukkit.command.ConsoleCommandSender

interface ConsoleCommand : BaseCommand {

    fun execute(consoleCommandSender: ConsoleCommandSender, args: List<String>)

    fun onCommand(consoleCommandSender: ConsoleCommandSender, args: List<String>) {
        checkConfig()
        checkArgs(consoleCommandSender, args)
        execute(consoleCommandSender, args)
    }
}
