package com.masahirosaito.spigot.homes.tests.exceptions

import org.bukkit.OfflinePlayer

class PlayerHomeIsPrivateException(player: OfflinePlayer, name: String? = null) : Exception(buildString {
    append("${player.name}'s ")
    append(if (name == null) "default home " else "home named $name ")
    append("is PRIVATE")
})
