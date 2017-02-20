package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.entity.Player

class SetCommand(
        val plugin: Homes,
        override val name: String = "set",
        override val permission: String = ""
) : SubCommand {

    override fun execute(player: Player, args: List<String>) {
        val playerHome = (plugin.homedata.playerHomes[player.uniqueId] ?: PlayerHome()).apply {
            defaultHome = LocationData.new(player.location)
        }

        plugin.homedata.apply {
            playerHomes.put(player.uniqueId, playerHome)
        }
    }
}