package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.exceptions.HomesException
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

interface BaseCommand {
    val plugin: Homes
    val name: String
    val description: String
    val usage: CommandUsage
    val commands: List<BaseCommand>

    fun configs(): List<Boolean>

    fun isValidArgs(args: List<String>): Boolean

    fun send(sender: CommandSender, message: String) {
        if (!message.isNullOrBlank()) plugin.messenger.send(sender, message)
    }

    fun send(sender: CommandSender, obj: Any) {
        send(sender, obj.toString())
    }

    fun checkConfig() {
        if (configs().contains(false)) throw HomesException("Not allowed by the configuration of this server")
    }

    fun checkArgs(args: List<String>) {
        if (!isValidArgs(args)) throw HomesException("The argument is incorrect\n$usage")
    }

    fun executeCommand(sender: CommandSender, args: List<String>) {
        val cmd = findCommand(args)
        when {
            cmd is PlayerCommand && sender is Player -> cmd.onCommand(sender, args)
            cmd is ConsoleCommand && sender is ConsoleCommandSender -> cmd.onCommand(sender, args)
            else -> throw HomesException("CommandSender is invalid")
        }
    }

    private fun findCommand(args: List<String>): BaseCommand {
        return commands.find { it.isValidArgs(args) } ?: this
    }
}
