package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.*
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

    @EventHandler(priority = EventPriority.MONITOR)
    fun checkTeleportMeta(event: PlayerJoinEvent) {
        if (DelayTeleporter.isAlreadyRun(event.player)) {
            DelayTeleporter.cancelTeleport(event.player)
        }
    }
}
