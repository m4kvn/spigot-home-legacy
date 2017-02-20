package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SetCommand(override val plugin: Homes) : SubCommand {
    override val name: String = "set"
    override val permission: String = "home.command.set"

    override fun execute(player: Player, args: List<String>): Boolean {

        val playerHome = plugin.homedata.playerHomes[player.uniqueId] ?: PlayerHome()

        if (args.isEmpty()) {
            playerHome.defaultHome = LocationData.new(player.location)
            plugin.messenger.send(player, buildString {
                append(ChatColor.AQUA)
                append("Set default home")
                append(ChatColor.RESET)
            })
        } else {
            if (!plugin.configs.onNamedHome) return true

            playerHome.namedHomes.put(args[0], LocationData.new(player.location))
            plugin.messenger.send(player, buildString {
                append(ChatColor.AQUA)
                append("Set home <${ChatColor.RESET}${args[0]}${ChatColor.AQUA}>")
                append(ChatColor.RESET)
            })
        }

        plugin.homedata.playerHomes.put(player.uniqueId, playerHome)

        return true
    }
}