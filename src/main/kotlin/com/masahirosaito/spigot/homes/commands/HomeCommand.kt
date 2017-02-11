package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.subcommands.HomeSetCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomeCommand(plugin: Homes) : CommandExecutor {
    val homeManager = plugin.homeManager
    val subCommands = mapOf(
            "set" to HomeSetCommand(plugin)
    )

    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {

        if (sender !is Player) return true

        if (args == null || args.isEmpty()) return execute(sender)

        if (subCommands.containsKey(args[0])) {
            return subCommands[args[0]]?.execute(sender)!!
        }

        return true
    }

    fun execute(player: Player): Boolean {
        val playerHomes = homeManager.getPlayerHomes(player.uniqueId)
        val defaultHome = playerHomes.defaultHome

        if (defaultHome.location == null) {
            player.sendMessage("ホームが設定されていません")
        } else {
            player.teleport(defaultHome.location)
        }

        return true
    }
}