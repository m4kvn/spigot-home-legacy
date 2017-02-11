package com.masahirosaito.spigot.homes.database

import org.jetbrains.exposed.sql.Table

object PlayerHomesObject : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val playerUid = uuid("player_uuid").uniqueIndex()
    val json = text("json")
}