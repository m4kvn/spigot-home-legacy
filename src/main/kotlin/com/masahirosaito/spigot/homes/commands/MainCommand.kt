package com.masahirosaito.spigot.homes.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

interface MainCommand : CommandExecutor, BaseCommand {
    val subCommands: List<BaseCommand>

    override fun onCommand(sender: CommandSender?, command: Command?,
                           label: String?, args: Array<out String>?): Boolean {
        val argsList = args?.toList() ?: emptyList()

        try {
            if (argsList.isNotEmpty() && subCommands.any { it.name == argsList[0] }) {
                subCommands.find { it.name == argsList[0] }!!.let {
                    it.executeCommand(sender!!, argsList.drop(1))
                }
            } else {
                executeCommand(sender!!, argsList)
            }
        } catch(e: Exception) {
            send(sender!!, buildString {
                append(ChatColor.RED)
                append(e.message)
                append(ChatColor.RESET)
            })
        }

        return true
    }
}
