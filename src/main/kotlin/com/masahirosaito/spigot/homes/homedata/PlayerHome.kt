package com.masahirosaito.spigot.homes.homedata

import com.masahirosaito.spigot.homes.datas.PlayerData
import com.masahirosaito.spigot.homes.nms.NMSController
import org.bukkit.OfflinePlayer

data class PlayerHome(
        var defaultHomeData: HomeData? = null,
        val namedHomeData: MutableList<HomeData> = mutableListOf()
) {
    fun toPlayerData(op: OfflinePlayer): PlayerData {
        return PlayerData(op).apply {
            defaultHomeData?.let { defaultHome = it.toHomesEntity(op) }
            namedHomeData.forEach { namedHomes.add(it.toHomesEntity(op)) }
        }
    }
}
