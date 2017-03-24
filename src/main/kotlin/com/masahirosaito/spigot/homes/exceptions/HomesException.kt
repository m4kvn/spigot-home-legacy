package com.masahirosaito.spigot.homes.exceptions

import org.bukkit.ChatColor

open class HomesException(msg: String) : Exception(msg) {

    fun getColorMsg(): String = buildString {
        append(ChatColor.RED)
        append(message)
        append(ChatColor.RESET)
    }
}
