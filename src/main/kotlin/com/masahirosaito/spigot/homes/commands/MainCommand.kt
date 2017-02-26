package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.exceptions.InValidCommandSenderException
import com.masahirosaito.spigot.homes.exceptions.NoSuchCommandException
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

abstract class MainCommand(override val plugin: Homes) : SubCommand, CommandExecutor {

    abstract val subCommands: List<SubCommand>

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        try {
            when {
                sender !is Player -> throw InValidCommandSenderException()
                args == null || args.isEmpty() -> onCommand(sender, emptyList())
                subCommands.any { it.name() == args[0] } -> onSubCommand(sender, args.toList())
                else -> onCommand(sender, args.toList())
            }
        } catch (e: Exception) {
            plugin.messenger.send(sender!!, buildString {
                append(ChatColor.RED)
                append(e.message)
                append(ChatColor.RESET)
            })
        }
        return true
    }

    fun onSubCommand(player: Player, args: List<String>) {
        findSubCommand(args[0]).onCommand(player, args.drop(1))
    }

    fun findSubCommand(name: String): SubCommand {
        return subCommands.find { it.name() == name } ?: throw NoSuchCommandException(name)
    }
}
