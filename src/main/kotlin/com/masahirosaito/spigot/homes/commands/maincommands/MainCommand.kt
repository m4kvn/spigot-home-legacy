package com.masahirosaito.spigot.homes.commands.maincommands

import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.exceptions.HomesException
import com.masahirosaito.spigot.homes.exceptions.NoConsoleCommandException
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

interface MainCommand : CommandExecutor, BaseCommand {
    val playerSubCommands: List<PlayerCommand>
    val consoleSubCommands: List<ConsoleCommand>

    override fun onCommand(sender: CommandSender?, command: Command?,
                           label: String?, args: Array<out String>?): Boolean {
        val argsList = args?.toList() ?: emptyList()

        try {
            if (sender is Player) {
                if (argsList.isNotEmpty() && playerSubCommands.any { it.name == argsList[0] }) {
                    playerSubCommands.find { it.name == argsList[0] }!!.executeCommand(sender, argsList.drop(1))
                } else {
                    executeCommand(sender, argsList)
                }
            }
            when (sender) {
                is Player -> {
                    if (argsList.isNotEmpty() && playerSubCommands.any { it.name == argsList[0] }) {
                        playerSubCommands.find { it.name == argsList[0] }!!.executeCommand(sender, argsList.drop(1))
                    } else {
                        executeCommand(sender, argsList)
                    }
                }
                is ConsoleCommandSender -> {
                    if (argsList.isNotEmpty() && consoleSubCommands.any { it.name == argsList[0] }) {
                        consoleSubCommands.find { it.name == argsList[0] }!!.executeCommand(sender, argsList.drop(1))
                    } else {
                        throw NoConsoleCommandException()
                    }
                }
            }
        } catch (e: HomesException) {
            send(sender!!, e.message)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }
}
