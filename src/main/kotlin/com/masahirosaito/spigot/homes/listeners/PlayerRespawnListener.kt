package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.PlayerDataManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawnListener(override val plugin: Homes) : HomesListener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerRespawn(event: PlayerRespawnEvent) {

        if (!Configs.onDefaultHomeRespawn) return

        try {
            event.respawnLocation = PlayerDataManager.findDefaultHome(event.player).location
        } catch (_: Exception) {
        }
    }
}
