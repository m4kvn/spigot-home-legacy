package com.masahirosaito.spigot.homes.exceptions

import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer

class PlayerHomeIsPrivateException(player: OfflinePlayer, name: String? = null) : Exception(buildString {
    append("${player.name}'s ")
    append(if (name == null) "default home" else "home named <${ChatColor.RESET}$name${ChatColor.RED}>")
    append(" is ${ChatColor.YELLOW}PRIVATE${ChatColor.RESET}")
})
