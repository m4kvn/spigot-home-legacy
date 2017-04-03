package com.masahirosaito.spigot.homes

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

object Messenger {
    lateinit var plugin: JavaPlugin

    fun load(homes: Homes) {
        plugin = homes
    }

    fun prefix(obj: Any): String = ChatColor.translateAlternateColorCodes('&', "[${plugin.name}] $obj")

    fun log(obj: Any) = Bukkit.getServer().consoleSender.sendMessage(prefix(obj))

    fun debug(obj: Any) {
        if (Configs.onDebug) log("${ChatColor.AQUA}[DEBUG]${ChatColor.RESET} $obj")
    }

    fun send(player: CommandSender, obj: Any) = player.sendMessage(prefix(obj))
}
