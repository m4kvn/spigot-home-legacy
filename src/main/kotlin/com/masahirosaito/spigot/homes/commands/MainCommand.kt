package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.SubCommand
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
        val list = args?.toList() ?: emptyList()
        if (sender !is Player) throw InValidCommandSenderException()
        try {
            when {
                haveSubCommand(list) -> onSubCommand(sender, list)
                else -> onCommand(sender, list)
            }
        } catch (e: Exception) {
            plugin.messenger.send(sender, buildString {
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

    fun haveSubCommand(args: List<String>): Boolean {
        return if (args.isEmpty()) false else subCommands.any { it.name() == args.first() }
    }
}
