package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawnListener(override val plugin: Homes) : HomesListener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerRespawn(event: PlayerRespawnEvent) {

        if (!plugin.configs.onDefaultHomeRespawn) return

        val playerHome = plugin.homeManager.playerHomes[event.player.uniqueId] ?: return
        val defaultHomeData = playerHome.defaultHomeData ?: return

        event.respawnLocation = defaultHomeData.locationData.toLocation()
    }
}
