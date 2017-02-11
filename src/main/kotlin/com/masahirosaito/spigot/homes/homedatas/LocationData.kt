package com.masahirosaito.spigot.homes.homedatas

import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

data class LocationData(
        val worldUID: UUID,
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float,
        val pitch: Float
) {
    fun toLocation(): Location = Location(Bukkit.getWorld(worldUID), x, y, z, yaw, pitch)

    companion object {
        fun fromLocation(loc: Location): LocationData {
            return LocationData(loc.world.uid, loc.x, loc.y, loc.z, loc.yaw, loc.pitch)
        }
    }
}