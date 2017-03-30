package com.masahirosaito.spigot.homes.nms

import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.toData
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.OfflinePlayer

class HomesEntity(
        val nmsManager: NMSManager,
        val offlinePlayer: OfflinePlayer,
        val location: Location,
        var homeName: String? = null,
        var isPrivate: Boolean = false
) {
    var entities: List<NMSEntityArmorStand> = nmsManager.spawn(this)

    fun isOwner(offlinePlayer: OfflinePlayer): Boolean {
        return this.offlinePlayer.uniqueId == offlinePlayer.uniqueId
    }

    fun toHomeData(): HomeData {
        return HomeData(homeName, location.toData(), isPrivate)
    }

    fun despawnEntities() {
        entities.forEach { it.dead() }
    }

    fun reSpawnEntities() {
        despawnEntities()
        entities = nmsManager.spawn(this)
    }

    fun inChunk(chunk: Chunk): Boolean {
        return chunk.x == location.chunk.x && chunk.z == location.chunk.z
    }

    fun changePrivate(isPrivate: Boolean) {
        this.isPrivate = isPrivate
        if (location.chunk.isLoaded) {
            reSpawnEntities()
        }
    }
}
