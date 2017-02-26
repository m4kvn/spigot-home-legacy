package com.masahirosaito.spigot.homes.homedata

import com.masahirosaito.spigot.homes.exceptions.CanNotFindDefaultHomeException
import com.masahirosaito.spigot.homes.exceptions.CanNotFindNamedHomeException
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

data class PlayerHome(
        var defaultHomeData: HomeData? = null,
        val namedHomeData: MutableMap<String, HomeData> = mutableMapOf()
) {
    fun findNamedHome(player: OfflinePlayer, name: String) = namedHomeData[name] ?:
            throw CanNotFindNamedHomeException(player, name)

    fun findDefaultHome(player: OfflinePlayer) = defaultHomeData ?:
            throw CanNotFindDefaultHomeException(player)

    fun removeNamedHome(player: OfflinePlayer, name: String) {
        findNamedHome(player, name)
        namedHomeData.remove(name)
    }

    fun removeDefaultHome(player: OfflinePlayer) {
        findDefaultHome(player)
        defaultHomeData = null
    }

    fun haveDefault(): Boolean = defaultHomeData != null

    fun haveName(name: String): Boolean = namedHomeData.containsKey(name)

    fun setDefaultHome(player: Player) {
        defaultHomeData = HomeData(LocationData.new(player.location))
    }

    fun setNamedHome(player: Player, name: String, limit: Int) {
        if (!haveName(name) && namedHomeData.isLimit(limit)) throwLimitException(limit)
        namedHomeData.put(name, HomeData(LocationData.new(player.location)))
    }

    private fun MutableMap<String, HomeData>.isLimit(limit: Int): Boolean = size >= limit

    private fun throwLimitException(limit: Int) {
        throw Exception("You can not set more homes (Limit: ${ChatColor.RESET}$limit${ChatColor.RED})")
    }
}
