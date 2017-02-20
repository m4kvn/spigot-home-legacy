package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.entity.Player

class SetCommand(val plugin: Homes) : SubCommand {
    override val name: String = "set"
    override val permission: String = "home.command.set"

    override fun execute(player: Player, args: List<String>) {

        val playerHome = plugin.homedata.playerHomes[player.uniqueId] ?: PlayerHome()

        if (args.isEmpty()) {
            playerHome.defaultHome = LocationData.new(player.location)
        } else {
            playerHome.namedHomes.put(args[0], LocationData.new(player.location))
        }
        
        plugin.homedata.playerHomes.put(player.uniqueId, playerHome)
    }
}