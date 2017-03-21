package com.masahirosaito.spigot.homes.homedata

import com.masahirosaito.spigot.homes.exceptions.CanNotFindDefaultHomeException
import com.masahirosaito.spigot.homes.exceptions.CanNotFindNamedHomeException
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

data class PlayerHome(
        var defaultHomeData: HomeData? = null,
        val namedHomeData: MutableList<HomeData> = mutableListOf()) {

    fun findNamedHome(player: OfflinePlayer, name: String) = namedHomeData.find { it.name == name } ?:
            throw CanNotFindNamedHomeException(player, name)

    fun findDefaultHome(player: OfflinePlayer) = defaultHomeData ?:
            throw CanNotFindDefaultHomeException(player)

    fun removeNamedHome(player: OfflinePlayer, name: String) {
        namedHomeData.remove(findNamedHome(player, name))
    }

    fun removeDefaultHome(player: OfflinePlayer) {
        findDefaultHome(player)
        defaultHomeData = null
    }

    fun haveName(name: String): Boolean = namedHomeData.any { it.name == name }

    fun setDefaultHome(player: Player) {
        defaultHomeData = HomeData(player.uniqueId, "default", LocationData.new(player.location))
    }

    fun setNamedHome(player: Player, name: String, limit: Int) {
        if (!haveName(name) && limit != -1 && namedHomeData.isLimit(limit)) throwLimitException(limit)
        if (haveName(name)) findNamedHome(player, name).locationData = LocationData.new(player.location)
        else namedHomeData.add(HomeData(player.uniqueId, name, LocationData.new(player.location)))
    }

    private fun MutableList<HomeData>.isLimit(limit: Int): Boolean = size >= limit

    private fun throwLimitException(limit: Int) {
        throw Exception("You can not set more homes (Limit: ${ChatColor.RESET}$limit${ChatColor.RED})")
    }
}
