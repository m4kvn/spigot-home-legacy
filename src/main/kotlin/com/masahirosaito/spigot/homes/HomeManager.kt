package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.homedatas.PlayerHomes
import java.util.*

class HomeManager {
    private val homes = mutableMapOf<UUID, PlayerHomes>()

    fun putHome(uuid: UUID, playerHomes: PlayerHomes) = homes.put(uuid, playerHomes)

    fun getPlayerHomes(uuid: UUID) = homes[uuid] ?: PlayerHomes().apply { putHome(uuid, this) }
}