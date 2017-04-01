package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.Messenger
import com.masahirosaito.spigot.homes.Permission
import com.masahirosaito.spigot.homes.UpdateChecker
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(override val plugin: Homes) : HomesListener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!event.player.hasPermission(Permission.home_update)) return
        if (UpdateChecker.isUpdate) {
            Messenger.send(event.player, "${ChatColor.YELLOW}${UpdateChecker.updateMessage}${ChatColor.RESET}")
            Messenger.send(event.player, "${ChatColor.YELLOW}${UpdateChecker.urlMessage}${ChatColor.RESET}")
        }
    }
}
