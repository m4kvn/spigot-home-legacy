package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.database.PlayerHomesObject
import com.masahirosaito.spigot.homes.homedatas.PlayerHomes
import org.bukkit.ChatColor
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class HomeManager(plugin: Homes) {
    private val messenger = plugin.messenger
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
        val sb = StringBuilder("\n")
                .appendln("${ChatColor.BLUE}---------- Save PlayerHomes ----------${ChatColor.RESET}")
        transaction {
            val uuidList = PlayerHomesObject.selectAll().map { it[PlayerHomesObject.playerUid] }

            homes.forEach { home ->
                try {
                    if (uuidList.contains(home.key)) {
                        PlayerHomesObject.update({ PlayerHomesObject.playerUid eq home.key }) {
                            it[json] = home.value.toJson()
                        }
                        sb.appendln("${ChatColor.GOLD}[UPDATE]${ChatColor.RESET} $home")
                    } else {
                        PlayerHomesObject.insert {
                            it[playerUid] = home.key
                            it[json] = home.value.toJson()
                        }
                        sb.appendln("${ChatColor.BLUE}[INSERT]${ChatColor.RESET} $home")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        messenger.debug(
                sb.append("${ChatColor.BLUE}----------- Save Complete! -----------${ChatColor.RESET}").toString())
    }

    fun load() {
        val sb = StringBuilder("\n")
                .appendln("${ChatColor.GREEN}---------- Load PlayerHomes ----------${ChatColor.RESET}")
        transaction {
            PlayerHomesObject.selectAll().forEach {
                try {
                    homes.put(it[PlayerHomesObject.playerUid], PlayerHomes.fromJson(it[PlayerHomesObject.json]))
                    sb.appendln("${ChatColor.GREEN}[LOAD]${ChatColor.RESET} ${homes[it[PlayerHomesObject.playerUid]]}")
                } catch(e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        messenger.debug(
                sb.append("${ChatColor.GREEN}----------- Load Complete! -----------${ChatColor.RESET}").toString())
    }
}