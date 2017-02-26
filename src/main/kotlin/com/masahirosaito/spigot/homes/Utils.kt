package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.exceptions.CanNotFindOfflinePlayerException
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

fun findOfflinePlayer(name: String): OfflinePlayer {
    return Bukkit.getOfflinePlayers().find { it.name == name } ?:
            throw CanNotFindOfflinePlayerException(name)
}
