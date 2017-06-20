package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.DelayTeleporter
import com.masahirosaito.spigot.homes.Homes
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveListener(override val plugin: Homes) : HomesListener {

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (DelayTeleporter.isAlreadyRun(event.player)) {
            DelayTeleporter.cancelTeleport(event.player)
        }
    }
}
