package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkLoadEvent

class ChunkLoadListener(override val plugin: Homes) : HomesListener {

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        plugin.playerDataManager.getHomesEntitiesIn(event.chunk).forEach { it.reSpawnEntities() }
    }
}
