package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.homedata.PlayerHome
import com.masahirosaito.spigot.homes.nms.HomesEntity
import org.bukkit.OfflinePlayer

class PlayerData(
        val offlinePlayer: OfflinePlayer,
        var defaultHome: HomesEntity? = null,
        val namedHomes: MutableList<HomesEntity> = mutableListOf()
) {
    fun tearDown() {
        defaultHome?.despawnEntities()
        namedHomes.forEach { it.despawnEntities() }
    }

    fun toPlayerHome(): PlayerHome = PlayerHome().apply {
        defaultHome?.let { defaultHomeData = it.toHomeData() }
        namedHomes.forEach { namedHomeData.add(it.toHomeData()) }
    }
}
