package com.masahirosaito.spigot.homes

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Messenger(val plugin: JavaPlugin, val onDebug: Boolean) {

    fun prefix(obj: Any) = "[${plugin.name}] $obj"

    fun log(obj: Any) = Bukkit.getServer().consoleSender.sendMessage(prefix(obj))

    fun debug(obj: Any) {
        if (onDebug) log("${ChatColor.AQUA}[DEBUG]${ChatColor.RESET} $obj")
    }

    fun send(player: CommandSender, obj: Any) = player.sendMessage(prefix(obj))
}