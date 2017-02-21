package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawnListener(override val plugin: Homes) : HomesListener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val playerHome = plugin.homedata.playerHomes[event.player.uniqueId] ?: return
        val defaultHome = playerHome.defaultHome ?: return

        event.respawnLocation = defaultHome.toLocation()
    }
}