package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.PlayerDataManager
import org.bukkit.event.EventHandler
import org.bukkit.event.world.ChunkLoadEvent

class ChunkLoadListener(override val plugin: Homes) : HomesListener {

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        PlayerDataManager.getHomesEntitiesIn(event.chunk).forEach { it.reSpawnEntities() }
    }
}
