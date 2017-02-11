package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.database.PlayerHomesObject
import com.masahirosaito.spigot.homes.homedatas.PlayerHomes
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class HomeManager(val plugin: Homes) {
    private val homes = mutableMapOf<UUID, PlayerHomes>()

    init {
        Database.connect("jdbc:h2:./${plugin.dataFolder.path}/${plugin.name}", driver = "org.h2.Driver")

        transaction {
            SchemaUtils.createMissingTablesAndColumns(PlayerHomesObject)
            load()
        }
    }

    fun putHome(uuid: UUID, playerHomes: PlayerHomes) = homes.put(uuid, playerHomes)

    fun getPlayerHomes(uuid: UUID) = homes[uuid] ?: PlayerHomes().apply { putHome(uuid, this) }

    fun save() {
        val sb = StringBuilder("\n").appendln("--- Save PlayerHomes ---")
        transaction {
            val uuidList = PlayerHomesObject.selectAll().map { it[PlayerHomesObject.playerUid] }

            homes.forEach { home ->
                try {
                    if (uuidList.contains(home.key)) {
                        PlayerHomesObject.update({ PlayerHomesObject.playerUid eq home.key }) {
                            it[json] = home.value.toJson()
                        }
                        sb.appendln("UPDATE: $home")
                    } else {
                        PlayerHomesObject.insert {
                            it[playerUid] = home.key
                            it[json] = home.value.toJson()
                        }
                        sb.appendln("INSERT: $home")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        plugin.logger.info(sb.append("--- Save Complete ---").toString())
    }

    fun load() {
        val sb = StringBuilder("\n").appendln("--- Load PlayerHomes ---")
        transaction {
            PlayerHomesObject.selectAll().forEach {
                try {
                    homes.put(it[PlayerHomesObject.playerUid], PlayerHomes.fromJson(it[PlayerHomesObject.json]))
                    sb.appendln("LOAD: ${homes[it[PlayerHomesObject.playerUid]]}")
                } catch(e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        plugin.logger.info(sb.append("--- Load Complete ---").toString())
    }
}