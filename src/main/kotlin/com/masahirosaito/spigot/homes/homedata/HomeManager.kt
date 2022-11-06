package com.masahirosaito.spigot.homes.homedata

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.masahirosaito.spigot.homes.datas.PlayerData
import com.masahirosaito.spigot.homes.findOfflinePlayer
import java.io.File
import java.util.*

data class HomeManager(val playerHomes: MutableMap<UUID, PlayerHome> = mutableMapOf()) {

    private fun toJson(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

    fun save(file: File) = file.writeText(toJson())

    companion object {
        fun load(file: File): HomeManager {
            return Gson().fromJson(file.readText().let {
                it.ifBlank { HomeManager().toJson() }
            }, HomeManager::class.java).apply { save(file) }
        }
    }

    fun toPlayerDataList() = mutableListOf<PlayerData>().apply {
        playerHomes.forEach { (uuid, playerHome) ->
            add(playerHome.toPlayerData(findOfflinePlayer(uuid)))
        }
    }
}
