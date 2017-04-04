package com.masahirosaito.spigot.homes.commands.maincommands

import com.masahirosaito.spigot.homes.commands.BaseCommand
import com.masahirosaito.spigot.homes.commands.subcommands.player.PlayerCommand
import com.masahirosaito.spigot.homes.commands.subcommands.console.ConsoleCommand
import com.masahirosaito.spigot.homes.exceptions.HomesException
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
            when (sender) {
                is Player -> executeCommand(sender, argsList, playerSubCommands)
                is ConsoleCommandSender -> executeCommand(sender, argsList, consoleSubCommands)
            }
        } catch (e: HomesException) {
            send(sender!!, e.message)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

    private fun <T : BaseCommand> executeCommand(sender: CommandSender, args: List<String>, commands: List<T>) {
        if (args.isNotEmpty() && commands.any { it.name == args[0] }) {
            commands.find { it.name == args[0] }!!.executeCommand(sender, args.drop(1))
        } else {
            executeCommand(sender, args)
        }
    }
}
