package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMoveListener(override val plugin: Homes) : HomesListener {
    val HOME_DELAY_META: String by lazy { HomeCommand.HOME_DELAY_META }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!event.player.hasMetadata(HOME_DELAY_META)) return

        (event.player.getMetadata(HOME_DELAY_META).first().value() as Thread).run {
            if (isAlive) {
                interrupt()
            }
        }
    }
}
