package com.masahirosaito.spigot.homes.homedata

import com.masahirosaito.spigot.homes.exceptions.PlayerHomeIsPrivateException
import org.bukkit.OfflinePlayer

data class HomeData(
        var locationData: LocationData,
        var isPrivate: Boolean = false
) {
    fun location() = locationData.toLocation()

    fun checkPrivate(offlinePlayer: OfflinePlayer, homeName: String? = null) = this.apply {
        if (isPrivate) throw PlayerHomeIsPrivateException(offlinePlayer, homeName)
    }
}
