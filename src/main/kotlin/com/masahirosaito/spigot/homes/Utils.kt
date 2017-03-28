package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.exceptions.CanNotFindOfflinePlayerException
import com.masahirosaito.spigot.homes.exceptions.CanNotFindOnlinePlayerException
import com.masahirosaito.spigot.homes.exceptions.HomesException
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

fun findOfflinePlayer(name: String): OfflinePlayer {
    return Bukkit.getOfflinePlayers().find { it.name == name } ?:
            throw CanNotFindOfflinePlayerException(name)
}

fun findOnlinePlayer(name: String): Player {
    return Bukkit.getOnlinePlayers().find { it.name == name } ?:
            throw CanNotFindOnlinePlayerException(name)
}

fun findOfflinePlayer(uuid: UUID): OfflinePlayer {
    return Bukkit.getOfflinePlayer(uuid) ?: throw  Exception("Player is not exist")
}

fun findOnlinePlayer(uuid: UUID): Player {
    return Bukkit.getPlayer(uuid) ?: throw Exception("Player is not online")
}

fun Player.teleportDefaultHome(plugin: Homes, offlinePlayer: OfflinePlayer) {
    val homeData = offlinePlayer.findDefaultHome(plugin)
    if (!homeData.isOwner(this) && homeData.isPrivate)
        throw HomesException("${offlinePlayer.name}'s default home is PRIVATE")
    teleport(homeData.location())
}

fun Player.teleportNamedHome(plugin: Homes, offlinePlayer: OfflinePlayer, homeName: String) {
    val homeData = offlinePlayer.findNamedHome(plugin, homeName)
    if (!homeData.isOwner(this) && homeData.isPrivate)
        throw HomesException("${offlinePlayer.name}'s home named <${homeData.name}> is PRIVATE")
    teleport(homeData.location())
}

fun OfflinePlayer.findDefaultHome(plugin: Homes) = plugin.homeManager.findDefaultHome(this)

fun OfflinePlayer.findNamedHome(plugin: Homes, homeName: String) = plugin.homeManager.findNamedHome(this, homeName)

fun OfflinePlayer.findPlayerHome(plugin: Homes) = plugin.homeManager.findPlayerHome(this)

fun getPrivateStatic(clazz: Class<*>, f: String): Any? {
    try {
        val field = clazz.getDeclaredField(f)
        field.isAccessible = true
        return field.get(null)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
