package com.masahirosaito.spigot.homes.tests.utils

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.tests.utils.MockWorldFactory.makeRandomLocation
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.metadata.MetadataValue
import org.bukkit.plugin.java.JavaPlugin
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.powermock.api.mockito.PowerMockito
import java.util.*
import java.util.logging.Logger

object MockPlayerFactory {
    val players: MutableMap<UUID, Player> = mutableMapOf()
    val locations: MutableMap<UUID, Location> = mutableMapOf()
    val permissions: MutableMap<UUID, MutableList<String>> = mutableMapOf()
    val offlinePlayers: MutableMap<UUID, OfflinePlayer> = mutableMapOf()
    val ops: MutableMap<UUID, Boolean> = mutableMapOf()
    val metadatas: MutableMap<UUID, MutableMap<String, MutableList<MetadataValue>>> = mutableMapOf()
    val loggers: MutableMap<UUID, SpyLogger> = mutableMapOf()

    fun makeNewMockPlayer(playerName: String, mockServer: Server): Player {
        return PowerMockito.mock(Player::class.java).apply {
            val uuid = UUID.randomUUID()
            PowerMockito.`when`(server).thenReturn(mockServer)
            PowerMockito.`when`(name).thenReturn(playerName)
            PowerMockito.`when`(uniqueId).thenReturn(uuid)
            PowerMockito.`when`(location).then { locations[uniqueId] }

            /* TODO: hasPermission(permission: String): Boolean */
            PowerMockito.`when`(hasPermission(anyString())).thenAnswer { invocation ->
                if (ops[uniqueId]!!) true else permissions[uniqueId]!!.contains(invocation.getArgumentAt(0, String::class.java))
            }

            /* TODO: getMetadata(metadataKey: String): List<MetadataValue> */
            PowerMockito.`when`(getMetadata(anyString())).thenAnswer { invocation ->
                metadatas[uniqueId]?.get(invocation.getArgumentAt(0, String::class.java))
            }

            /* TODO: setMetadata(metadataKey: String, newMetadataValue: MetadataValue): Unit */
            PowerMockito.`when`(setMetadata(anyString(), any(MetadataValue::class.java))).thenAnswer { invocation ->
                metadatas[uniqueId]?.put(
                        invocation.getArgumentAt(0, String::class.java),
                        mutableListOf(invocation.getArgumentAt(1, MetadataValue::class.java)))
            }

            /* TODO: hasMetadata(metadataKey: String): Boolean */
            PowerMockito.`when`(hasMetadata(anyString())).thenAnswer { invocation ->
                metadatas[uniqueId]?.contains(invocation.getArgumentAt(0, String::class.java)) ?: false
            }

            /* TODO: removeMetadata(metadataKey: String, plugin: JavaPlugin): Unit */
            PowerMockito.`when`(removeMetadata(anyString(), any(JavaPlugin::class.java))).thenAnswer { invocation ->
                metadatas[uniqueId]?.remove(invocation.getArgumentAt(0, String::class.java))
            }

            /* TODO: teleport(location: Location): Boolean */
            PowerMockito.`when`(teleport(any(Location::class.java))).thenAnswer { invocation ->
                locations.put(uniqueId, invocation.getArgumentAt(0, Location::class.java)); true
            }

            /* TODO: sendMessage(msg: String): Unit */
            PowerMockito.`when`(sendMessage(anyString())).thenAnswer { invocation ->
                loggers[uniqueId]?.info(invocation.getArgumentAt(0, String::class.java))
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
        players.put(player.uniqueId, player)
        locations.put(player.uniqueId, makeRandomLocation())
        permissions.put(player.uniqueId, mutableListOf())
        offlinePlayers.put(player.uniqueId, player)
        ops.put(player.uniqueId, false)
        metadatas.put(player.uniqueId, mutableMapOf())
        loggers.put(player.uniqueId, SpyLogger(Logger.getLogger(player.name)))
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
