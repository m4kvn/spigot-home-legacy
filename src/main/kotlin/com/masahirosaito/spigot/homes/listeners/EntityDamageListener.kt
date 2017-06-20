package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.DelayTeleporter
import com.masahirosaito.spigot.homes.Homes
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent

class EntityDamageListener(override val plugin: Homes) : HomesListener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity as? Player ?: return

        if (!Configs.onDamageCancel) return

        if (DelayTeleporter.isAlreadyRun(entity)) {
            DelayTeleporter.cancelTeleport(entity)
        }
    }
}
