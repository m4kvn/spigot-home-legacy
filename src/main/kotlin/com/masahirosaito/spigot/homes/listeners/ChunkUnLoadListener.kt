package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkUnloadEvent

class ChunkUnLoadListener(override val plugin: Homes) : HomesListener {

    @EventHandler
    fun onChunkUnLoad(event: ChunkUnloadEvent) {
        plugin.playerDataManager.getHomesEntitiesIn(event.chunk).forEach { it.despawnEntities() }
    }
}
