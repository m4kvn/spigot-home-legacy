package com.masahirosaito.spigot.homes.homedata

import com.masahirosaito.spigot.homes.nms.HomesEntity
import com.masahirosaito.spigot.homes.nms.NMSController
import org.bukkit.OfflinePlayer

data class HomeData(
        val name: String?,
        var locationData: LocationData,
        var isPrivate: Boolean = false
) {
    fun location() = locationData.toLocation()

    fun toHomesEntity(op: OfflinePlayer): HomesEntity {
        return HomesEntity(op, location(), name, isPrivate)
    }
}
