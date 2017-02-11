package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.entity.Player

class HomeSetCommand(plugin: Homes) {
    val homeManager = plugin.homeManager

    fun execute(player: Player): Boolean {
        homeManager.getPlayerHomes(player.uniqueId).defaultHome.location = player.location
        player.sendMessage("ホームを設定しました")
        return true
    }
}