package com.masahirosaito.spigot.homes.exceptions

import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer

class PlayerHomeIsPrivateException(player: OfflinePlayer, name: String) : Exception(buildString {
    append("${player.name}'s ")
    if (name.isNullOrBlank()) append("default ")
    append("home ")
    if (name.isNotBlank()) append("named ${ChatColor.RESET}$name${ChatColor.RED} ")
    append("is ${ChatColor.YELLOW}PRIVATE${ChatColor.RESET}")
})
