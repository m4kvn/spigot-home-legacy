package com.masahirosaito.spigot.homes.homedata

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.masahirosaito.spigot.homes.PlayerData
import com.masahirosaito.spigot.homes.PlayerDataManager
import com.masahirosaito.spigot.homes.findOfflinePlayer
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import com.masahirosaito.spigot.homes.nms.HomesEntity
import com.masahirosaito.spigot.homes.nms.NMSManager
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.io.File
import java.util.*

data class HomeManager(val playerHomes: MutableMap<UUID, PlayerHome> = mutableMapOf()) {

    private fun toJson(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

    fun save(file: File) = file.writeText(toJson())

    companion object {
        fun load(file: File): HomeManager {
            return Gson().fromJson(file.readText().let {
                if (it.isNullOrBlank()) HomeManager().toJson() else it
            }, HomeManager::class.java).apply { save(file) }
        }
    }

    fun toPlayerDatas(nmsManager: NMSManager) = mutableListOf<PlayerData>().apply {
        playerHomes.forEach { uuid, playerData ->
            add(playerData.toPlayerData(nmsManager, findOfflinePlayer(uuid)))
        }
    }
}
