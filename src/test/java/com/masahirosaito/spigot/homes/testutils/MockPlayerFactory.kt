package com.masahirosaito.spigot.homes.testutils

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.testutils.MockWorldFactory.makeRandomLocation
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.metadata.MetadataValue
import org.bukkit.plugin.java.JavaPlugin
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.*
import java.util.logging.Logger

object MockPlayerFactory {
    val players: MutableMap<UUID, Player> = mutableMapOf()
    private val locations: MutableMap<UUID, Location> = mutableMapOf()
    val permissions: MutableMap<UUID, MutableList<String>> = mutableMapOf()
    val offlinePlayers: MutableMap<UUID, OfflinePlayer> = mutableMapOf()
    val ops: MutableMap<UUID, Boolean> = mutableMapOf()
    private val metadatas: MutableMap<UUID, MutableMap<String, MutableList<MetadataValue>>> = mutableMapOf()
    val loggers: MutableMap<UUID, SpyLogger> = mutableMapOf()

    private fun makeNewMockPlayer(playerName: String, mockServer: Server): Player {
        return mock(Player::class.java).apply {
            val uuid = UUID.randomUUID()
            `when`(server).thenReturn(mockServer)
            `when`(name).thenReturn(playerName)
            `when`(uniqueId).thenReturn(uuid)
            `when`(location).then { locations[uniqueId] }

            /* TODO: hasPermission(permission: String): Boolean */
            `when`(hasPermission(anyString())).thenAnswer { invocation ->
                if (ops[uniqueId]!!) true else permissions[uniqueId]!!.contains(invocation.getArgument(0))
            }

            /* TODO: getMetadata(metadataKey: String): List<MetadataValue> */
            `when`(getMetadata(anyString())).thenAnswer { invocation ->
                metadatas[uniqueId]?.get(invocation.getArgument(0))
            }

            /* TODO: setMetadata(metadataKey: String, newMetadataValue: MetadataValue): Unit */
            `when`(setMetadata(anyString(), any(MetadataValue::class.java))).thenAnswer { invocation ->
                metadatas[uniqueId]?.put(
                    invocation.getArgument(0),
                    mutableListOf(invocation.getArgument(1))
                )
            }

            /* TODO: hasMetadata(metadataKey: String): Boolean */
            `when`(hasMetadata(anyString())).thenAnswer { invocation ->
                metadatas[uniqueId]?.contains(invocation.getArgument(0)) ?: false
            }

            /* TODO: removeMetadata(metadataKey: String, plugin: JavaPlugin): Unit */
            `when`(removeMetadata(anyString(), any(JavaPlugin::class.java))).thenAnswer { invocation ->
                metadatas[uniqueId]?.remove(invocation.getArgument(0))
            }

            /* TODO: teleport(location: Location): Boolean */
            `when`(teleport(any(Location::class.java))).thenAnswer { invocation ->
                locations[uniqueId] = invocation.getArgument(0); true
            }

            /* TODO: sendMessage(msg: String): Unit */
            `when`(sendMessage(anyString())).thenAnswer { invocation ->
                loggers[uniqueId]?.info(invocation.getArgument<String>(0))
            }

            register(this)
        }
    }

    fun makeNewMockPlayer(playerName: String, homes: Homes): Player {
        return makeNewMockPlayer(playerName, homes.server).apply {
            homes.econ?.createPlayerAccount(this)
        }
    }

    private fun register(player: Player) {
        players[player.uniqueId] = player
        locations[player.uniqueId] = makeRandomLocation()
        permissions[player.uniqueId] = mutableListOf()
        offlinePlayers[player.uniqueId] = player
        ops[player.uniqueId] = false
        metadatas[player.uniqueId] = mutableMapOf()
        loggers[player.uniqueId] = SpyLogger(Logger.getLogger(player.name))
    }

    fun clear() {
        players.clear()
        locations.clear()
        permissions.clear()
        offlinePlayers.clear()
        ops.clear()
        metadatas.clear()
        loggers.clear()
    }
}
