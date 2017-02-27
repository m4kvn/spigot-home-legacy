package com.masahirosaito.spigot.homes

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.masahirosaito.spigot.homes.exceptions.CanNotFindDefaultHomeException
import com.masahirosaito.spigot.homes.exceptions.CanNotFindNamedHomeException
import com.masahirosaito.spigot.homes.exceptions.CanNotFindPlayerHomeException
import com.masahirosaito.spigot.homes.homedata.LocationData
import com.masahirosaito.spigot.homes.homedata.PlayerHome
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
            }, HomeManager::class.java).apply { save(file)}
        }
    }

    fun findPlayerHome(player: OfflinePlayer) = playerHomes[player.uniqueId] ?:
            PlayerHome().apply { playerHomes.put(player.uniqueId, this) }

    fun findDefaultHome(player: OfflinePlayer) = findPlayerHome(player).findDefaultHome(player)

    fun findNamedHome(player: OfflinePlayer, name: String) = findPlayerHome(player).findNamedHome(player, name)
}
