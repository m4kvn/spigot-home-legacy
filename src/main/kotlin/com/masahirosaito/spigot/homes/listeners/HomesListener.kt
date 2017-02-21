package com.masahirosaito.spigot.homes.listeners

import com.masahirosaito.spigot.homes.Homes
import org.bukkit.event.Listener

interface HomesListener : Listener {
    val plugin: Homes

    fun register() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }
}