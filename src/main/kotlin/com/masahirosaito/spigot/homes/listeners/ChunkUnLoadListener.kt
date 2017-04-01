package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.PlayerDataManager
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkUnloadEvent

class ChunkUnLoadListener(override val plugin: Homes) : HomesListener {

    @EventHandler
    fun onChunkUnLoad(event: ChunkUnloadEvent) {
        PlayerDataManager.getHomesEntitiesIn(event.chunk).forEach { it.despawnEntities() }
    }
}
