package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawnListener(override val plugin: Homes) : HomesListener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerRespawn(event: PlayerRespawnEvent) {

        if (!plugin.configs.onDefaultHomeRespawn) return

        try {
            event.respawnLocation = plugin.playerDataManager.findDefaultHome(event.player).location
        } catch (e: Exception) {
        }
    }
}
