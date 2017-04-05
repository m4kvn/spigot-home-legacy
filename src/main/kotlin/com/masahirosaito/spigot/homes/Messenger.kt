package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.Homes.Companion.homes
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object Messenger {

    fun prefix(obj: Any): String = ChatColor.translateAlternateColorCodes('&', "[${homes.name}] $obj")

    fun log(obj: Any) = Bukkit.getServer().consoleSender.sendMessage(prefix(obj))

    fun debug(obj: Any) {
        if (Configs.onDebug) log("&b[DEBUG]&r $obj")
    }

    fun send(player: CommandSender, obj: Any) = player.sendMessage(prefix(obj))
}
