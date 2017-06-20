package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.datas.PlayerData
import com.masahirosaito.spigot.homes.exceptions.LimitHomeException
import com.masahirosaito.spigot.homes.exceptions.NoDefaultHomeException
import com.masahirosaito.spigot.homes.exceptions.NoNamedHomeException
import com.masahirosaito.spigot.homes.homedata.HomeManager
import com.masahirosaito.spigot.homes.nms.HomesEntity
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import java.io.File

object PlayerDataManager {
    private val playerHomeDataFile = File(homes.dataFolder, "playerhomes.json")
    private val playerDatas: MutableList<PlayerData> = mutableListOf()

    fun load() {
        playerHomeDataFile.load()
        playerDatas.addAll(HomeManager.load(playerHomeDataFile).toPlayerDatas())
        playerDatas.forEach { it.load() }
    }

    fun save(): PlayerDataManager = this.apply {
        tearDown().toHomeManager().save(playerHomeDataFile)
    }

    private fun toHomeManager(): HomeManager {
        return HomeManager().apply {
            playerDatas.forEach { playerHomes.put(it.offlinePlayer.uniqueId, it.toPlayerHome()) }
        }
    }

    private fun tearDown(): PlayerDataManager = this.apply {
        playerDatas.forEach { it.tearDown() }
    }

    fun getHomesEntitiesIn(chunk: Chunk): List<HomesEntity> {
        return mutableListOf<HomesEntity>().apply {
            playerDatas.forEach { addAll(it.getHomesEntitiesIn(chunk)) }
        }
    }

    fun findPlayerData(offlinePlayer: OfflinePlayer): PlayerData {
        return playerDatas.find { it.offlinePlayer.uniqueId == offlinePlayer.uniqueId } ?:
                PlayerData(offlinePlayer, null, mutableListOf()).apply { playerDatas.add(this) }
    }

    fun findDefaultHome(offlinePlayer: OfflinePlayer): HomesEntity {
        return findPlayerData(offlinePlayer).defaultHome ?:
                throw NoDefaultHomeException(offlinePlayer)
    }

    fun findNamedHome(offlinePlayer: OfflinePlayer, homeName: String): HomesEntity {
        return findPlayerData(offlinePlayer).getNamedHome(homeName)
    }

    fun hasDefaultHome(offlinePlayer: OfflinePlayer): Boolean {
        return findPlayerData(offlinePlayer).defaultHome != null
    }

    fun hasNamedHome(offlinePlayer: OfflinePlayer, homeName: String): Boolean {
        return findPlayerData(offlinePlayer).hasNamedHome(homeName)
    }

    fun setDefaultHome(offlinePlayer: OfflinePlayer, location: Location) {
        findPlayerData(offlinePlayer).let { playerData ->
            if (playerData.defaultHome != null) {
                removeDefaultHome(offlinePlayer)
            }
            playerData.defaultHome = HomesEntity(offlinePlayer, location).apply {
                spawnEntities()
            }
        }
    }

    fun setNamedHome(offlinePlayer: OfflinePlayer, location: Location, homeName: String) {
        findPlayerData(offlinePlayer).let { (_, _, namedHomes) ->
            if (hasNamedHome(offlinePlayer, homeName)) {
                removeNamedHome(offlinePlayer, homeName)
            }
            namedHomes.add(HomesEntity(offlinePlayer, location, homeName).apply {
                if (Configs.homeLimit > -1 && namedHomes.size >= Configs.homeLimit)
                    throw LimitHomeException(Configs.homeLimit)
                else spawnEntities()
            })
        }
    }

    fun removeDefaultHome(offlinePlayer: OfflinePlayer) {
        findPlayerData(offlinePlayer).removeDefaultHome()
    }

    fun removeNamedHome(offlinePlayer: OfflinePlayer, homeName: String) {
        findPlayerData(offlinePlayer).removeNamedHome(homeName)
    }

    fun setDefaultHomePrivate(offlinePlayer: OfflinePlayer, isPrivate: Boolean) {
        findDefaultHome(offlinePlayer).let {
            it.isPrivate = isPrivate
            if (it.location.chunk.isLoaded) {
                it.reSpawnEntities()
            }
        }
    }

    fun setNamedHomePrivate(offlinePlayer: OfflinePlayer, homeName: String, isPrivate: Boolean) {
        findNamedHome(offlinePlayer, homeName).let {
            it.isPrivate = isPrivate
            if (it.location.chunk.isLoaded) {
                it.reSpawnEntities()
            }
        }
    }

    fun getPlayerDataList(): List<PlayerData> = playerDatas
}
