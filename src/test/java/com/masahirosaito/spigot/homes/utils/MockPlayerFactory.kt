package com.masahirosaito.spigot.homes.utils

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.mockito.Matchers
import org.mockito.Matchers.any
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.doAnswer
import org.powermock.api.mockito.PowerMockito.mock
import java.util.*

object MockPlayerFactory {
    val players: MutableList<Player> = mutableListOf()
    val locations: MutableMap<UUID, Location> = mutableMapOf()
    val permissions: MutableMap<UUID, MutableList<String>> = mutableMapOf()

    fun makeNewMockPlayer(playerName: String): Player {
        return PowerMockito.mock(Player::class.java).apply {
            val uuid = UUID.randomUUID()
            PowerMockito.`when`(name).thenReturn(playerName)
            PowerMockito.`when`(hasPermission(Matchers.anyString())).thenReturn(true)
            PowerMockito.`when`(uniqueId).thenReturn(uuid)
            PowerMockito.`when`(location).then { locations[uniqueId] }
            doAnswer({ invocation ->
                locations.put(uniqueId, invocation.getArgumentAt(0, Location::class.java)); true
            }).`when`(this).teleport(any(Location::class.java))
            register(this)
        }
    }

    private fun register(player: Player) {
        players.add(player)
        locations.put(player.uniqueId, Location(mock(World::class.java), 0.0, 0.0, 0.0))
        permissions.put(player.uniqueId, mutableListOf())
    }

    fun clear() {
        players.clear()
        locations.clear()
        permissions.clear()
    }
}
