package com.masahirosaito.spigot.homes.utils

import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.entity.Player
import org.mockito.Matchers
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.doAnswer
import org.powermock.api.mockito.PowerMockito.mock
import java.util.*

object MockPlayerFactory {
    val players: MutableMap<UUID, Player> = mutableMapOf()
    val locations: MutableMap<UUID, Location> = mutableMapOf()
    val permissions: MutableMap<UUID, MutableList<String>> = mutableMapOf()
    val offlinePlayers: MutableMap<UUID, OfflinePlayer> = mutableMapOf()

    fun makeNewMockPlayer(playerName: String, mockServer: Server): Player {
        return PowerMockito.mock(Player::class.java).apply {
            val uuid = UUID.randomUUID()
            PowerMockito.`when`(server).thenReturn(mockServer)
            PowerMockito.`when`(name).thenReturn(playerName)
            PowerMockito.`when`(hasPermission(Matchers.anyString())).thenReturn(true)
            PowerMockito.`when`(uniqueId).thenReturn(uuid)
            PowerMockito.`when`(location).then { locations[uniqueId] }
            doAnswer({ invocation ->
                locations.put(uniqueId, invocation.getArgumentAt(0, Location::class.java)); true
            }).`when`(this).teleport(any(Location::class.java))
            doAnswer({ invocation ->
                server.logger.info(invocation.getArgumentAt(0, String::class.java))
            }).`when`(this).sendMessage(anyString())
            register(this)
        }
    }

    private fun register(player: Player) {
        players.put(player.uniqueId, player)
        locations.put(player.uniqueId, Location(mock(World::class.java), 0.0, 0.0, 0.0))
        permissions.put(player.uniqueId, mutableListOf())
        offlinePlayers.put(player.uniqueId, player)
    }

    fun clear() {
        players.clear()
        locations.clear()
        permissions.clear()
    }
}
