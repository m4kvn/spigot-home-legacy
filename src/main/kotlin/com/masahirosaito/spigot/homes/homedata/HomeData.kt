package com.masahirosaito.spigot.homes.homedata

import com.masahirosaito.spigot.homes.nms.HomesEntity
import com.masahirosaito.spigot.homes.nms.NMSManager
import org.bukkit.OfflinePlayer

data class HomeData(
        val name: String?,
        var locationData: LocationData,
        var isPrivate: Boolean = false
) {
    fun location() = locationData.toLocation()

    fun toHomesEntity(nmsManager: NMSManager, op: OfflinePlayer): HomesEntity {
        return HomesEntity(nmsManager, op, location(), name, isPrivate)
    }
}
