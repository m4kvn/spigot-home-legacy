package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.exceptions.CanNotFindOfflinePlayerException
import com.masahirosaito.spigot.homes.exceptions.CanNotFindOnlinePlayerException
import com.masahirosaito.spigot.homes.homedata.LocationData
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.io.File
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

fun File.load(): File = this.apply {
    if (!parentFile.exists()) parentFile.mkdirs()
    if (!exists()) createNewFile()
}

fun Location.toData(): LocationData = LocationData.new(this)
