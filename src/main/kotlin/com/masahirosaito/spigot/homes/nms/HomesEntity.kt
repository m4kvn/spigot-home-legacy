package com.masahirosaito.spigot.homes.nms

import com.masahirosaito.spigot.homes.homedata.HomeData
import com.masahirosaito.spigot.homes.toData
import org.bukkit.Location
import org.bukkit.OfflinePlayer

class HomesEntity(
        nmsManager: NMSManager,
        val offlinePlayer: OfflinePlayer,
        val location: Location,
        var homeName: String? = null,
        var isPrivate: Boolean = false
) {
    val entities: List<NMSEntityArmorStand> = nmsManager.spawn(this)

    fun isOwner(offlinePlayer: OfflinePlayer): Boolean {
        return this.offlinePlayer.uniqueId == offlinePlayer.uniqueId
    }

    fun toHomeData(): HomeData {
        return HomeData(homeName, location.toData(), isPrivate)
    }

    fun despawnEntities() {
        entities.forEach { it.dead() }
    }
}
