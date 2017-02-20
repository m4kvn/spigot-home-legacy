package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.subcommands.SetCommand
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomeCommand(val plugin: Homes) : CommandExecutor, SubCommand {
    override val name: String = "home"
    override val permission = "home.command"
    val subCommands = listOf<SubCommand>(SetCommand(plugin))

    override fun onCommand(sender: CommandSender?, command: Command?,
                           label: String?, args: Array<out String>?): Boolean {

        if (sender !is Player) return true

        if (!hasPermission(sender))  return sendPermissionMsg(sender, permission)

        if (args == null || args.isEmpty()) return execute(sender, emptyList()).let { true }

        subCommands.find { it.name == args[0] }?.let {
            if (it.hasPermission(sender)) it.execute(sender, args.drop(1))
            else return sendPermissionMsg(sender, it.permission)
        }

        return true
    }

    override fun execute(player: Player, args: List<String>) {
        plugin.homedata.playerHomes[player.uniqueId]?.let { playerHome ->
            playerHome.defaultHome?.let { player.teleport(it.toLocation()) }
        }
    }

    fun sendPermissionMsg(player: Player, permission: String): Boolean {
        plugin.messenger.send(player, buildString {
            append(ChatColor.RED)
            append("You don't have permission <$permission>")
            append(ChatColor.RESET)
        })
        return true
    }
}