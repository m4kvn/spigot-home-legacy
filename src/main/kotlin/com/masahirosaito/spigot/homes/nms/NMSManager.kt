package com.masahirosaito.spigot.homes.nms

import org.bukkit.Location

interface NMSManager {

    fun setUp()

    fun spawnNMSArmorStand(loc: Location, name: String)

}
