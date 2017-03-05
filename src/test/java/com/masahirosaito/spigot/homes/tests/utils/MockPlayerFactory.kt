package com.masahirosaito.spigot.homes.tests.utils

import com.masahirosaito.spigot.homes.tests.utils.MockWorldFactory.makeRandomLocation
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
    val ops: MutableList<UUID> = mutableListOf()

    fun makeNewMockPlayer(playerName: String, mockServer: Server): Player {
        return PowerMockito.mock(Player::class.java).apply {
            val uuid = UUID.randomUUID()
            PowerMockito.`when`(server).thenReturn(mockServer)
            PowerMockito.`when`(name).thenReturn(playerName)
            PowerMockito.`when`(uniqueId).thenReturn(uuid)
            PowerMockito.`when`(location).then { locations[uniqueId] }
            PowerMockito.`when`(hasPermission(anyString())).thenAnswer { invocation ->
                if (isOps()) true
                else permissions[uniqueId]?.contains(invocation.getArgumentAt(0, String::class.java))
            }
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
        locations.put(player.uniqueId, makeRandomLocation())
        permissions.put(player.uniqueId, mutableListOf())
        offlinePlayers.put(player.uniqueId, player)
    }

    fun clear() {
        players.clear()
        locations.clear()
        permissions.clear()
        offlinePlayers.clear()
        ops.clear()
    }
}
