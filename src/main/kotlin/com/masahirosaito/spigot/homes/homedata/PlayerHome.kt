package com.masahirosaito.spigot.homes.homedata

import com.masahirosaito.spigot.homes.PlayerData
import com.masahirosaito.spigot.homes.nms.NMSManager
import org.bukkit.OfflinePlayer

data class PlayerHome(
        var defaultHomeData: HomeData? = null,
        val namedHomeData: MutableList<HomeData> = mutableListOf()
) {
    fun toPlayerData(nmsManager: NMSManager, op: OfflinePlayer): PlayerData {
        return PlayerData(op).apply {
            defaultHomeData?.let { defaultHome = it.toHomesEntity(nmsManager, op) }
            namedHomeData.forEach { namedHomes.add(it.toHomesEntity(nmsManager, op)) }
        }
    }
}
