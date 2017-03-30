package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.exceptions.LimitHomeException
import com.masahirosaito.spigot.homes.exceptions.NoDefaultHomeException
import com.masahirosaito.spigot.homes.exceptions.NoNamedHomeException
import com.masahirosaito.spigot.homes.homedata.HomeManager
import com.masahirosaito.spigot.homes.nms.HomesEntity
import com.masahirosaito.spigot.homes.nms.NMSManager
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import java.io.File

class PlayerDataManager(val homes: Homes) {
    private val playerHomeDataFile = File(homes.dataFolder, "playerhomes.json").load()
    private val homeManager = HomeManager.load(playerHomeDataFile)
    private val nmsManager = NMSManager.load(homes)
    private val playerDatas: MutableList<PlayerData> = mutableListOf()

    fun load(): PlayerDataManager = this.apply {
        nmsManager.setUp()
        playerDatas.addAll(homeManager.toPlayerDatas(nmsManager))
    }

    fun save(): PlayerDataManager = this.apply {
        tearDown().toHomeManager().save(playerHomeDataFile)
    }

    private fun toHomeManager(): HomeManager {
        return HomeManager().apply {
            playerDatas.forEach { playerHomes.put(it.offlinePlayer.uniqueId, it.toPlayerHome()) }
        }
    }

    private fun tearDown(): PlayerDataManager = this.apply { playerDatas.forEach { it.tearDown() } }

    fun findPlayerData(offlinePlayer: OfflinePlayer): PlayerData {
        return playerDatas.find { it.offlinePlayer.uniqueId == offlinePlayer.uniqueId } ?:
                PlayerData(offlinePlayer, null, mutableListOf())
    }

    fun findDefaultHome(offlinePlayer: OfflinePlayer): HomesEntity {
        return findPlayerData(offlinePlayer).defaultHome ?:
                throw NoDefaultHomeException(offlinePlayer)
    }

    fun findNamedHome(offlinePlayer: OfflinePlayer, homeName: String): HomesEntity {
        return findPlayerData(offlinePlayer).namedHomes.find { it.homeName == homeName } ?:
                throw NoNamedHomeException(offlinePlayer, homeName)
    }

    fun hasNamedHome(offlinePlayer: OfflinePlayer, homeName: String): Boolean {
        return findPlayerData(offlinePlayer).namedHomes.any { it.homeName == homeName }
    }

    fun setDefaultHome(offlinePlayer: OfflinePlayer, location: Location) {
        findPlayerData(offlinePlayer).let { playerData ->
            if (playerData.defaultHome != null) {
                removeDefaultHome(offlinePlayer)
            }
            playerData.defaultHome = HomesEntity(nmsManager, offlinePlayer, location)
        }
    }

    fun setNamedHome(offlinePlayer: OfflinePlayer, location: Location, homeName: String) {
        findPlayerData(offlinePlayer).let { playerData ->
            if (hasNamedHome(offlinePlayer, homeName)) {
                val limit = homes.configs.homeLimit
                if (limit != -1 && playerData.namedHomes.size >= limit) throw LimitHomeException(limit)
                removeNamedHome(offlinePlayer, homeName)
            }
            playerData.namedHomes.add(HomesEntity(nmsManager, offlinePlayer, location, homeName))
        }
    }

    fun removeDefaultHome(offlinePlayer: OfflinePlayer) {
        findDefaultHome(offlinePlayer).despawnEntities()
        findPlayerData(offlinePlayer).defaultHome = null
    }

    fun removeNamedHome(offlinePlayer: OfflinePlayer, homeName: String) {
        findNamedHome(offlinePlayer, homeName).apply { despawnEntities() }.apply {
            findPlayerData(offlinePlayer).namedHomes.remove(this)
        }
    }
}
