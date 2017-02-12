package com.masahirosaito.spigot.homes.commands.subcommands

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.entity.Player

class HomeSetCommand(plugin: Homes) {
    val messenger = plugin.messenger
    val homeManager = plugin.homeManager

    fun execute(player: Player): Boolean {
        homeManager.getPlayerHomes(player.uniqueId).defaultHome.setLocation(player.location)
        messenger.send(player, "ホームを設定しました！")
        return true
    }
}