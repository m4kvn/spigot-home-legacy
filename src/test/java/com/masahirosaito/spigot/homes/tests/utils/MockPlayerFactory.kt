package com.masahirosaito.spigot.homes.tests.utils

import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.entity.Player
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.doAnswer
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
            PowerMockito.`when`(uniqueId).thenReturn(uuid)
            PowerMockito.`when`(location).then { MockPlayerFactory.locations[uniqueId] }
            PowerMockito.`when`(hasPermission(anyString())).thenAnswer { invocation ->
                MockPlayerFactory.permissions[uniqueId]?.contains(invocation.getArgumentAt(0, String::class.java))
            }
            doAnswer({ invocation ->
                MockPlayerFactory.locations.put(uniqueId, invocation.getArgumentAt(0, Location::class.java)); true
            }).`when`(this).teleport(any(Location::class.java))
            doAnswer({ invocation ->
                server.logger.info(invocation.getArgumentAt(0, String::class.java))
            }).`when`(this).sendMessage(anyString())
            MockPlayerFactory.register(this)
        }
    }

    private fun register(player: Player) {
        MockPlayerFactory.players.put(player.uniqueId, player)
        MockPlayerFactory.locations.put(player.uniqueId, MockWorldFactory.makeRandomLocation())
        MockPlayerFactory.permissions.put(player.uniqueId, mutableListOf())
        MockPlayerFactory.offlinePlayers.put(player.uniqueId, player)
    }

    fun clear() {
        MockPlayerFactory.players.clear()
        MockPlayerFactory.locations.clear()
        MockPlayerFactory.permissions.clear()
        MockPlayerFactory.offlinePlayers.clear()
    }
}
