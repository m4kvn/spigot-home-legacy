package com.masahirosaito.spigot.homes.homedata

import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

data class LocationData(
        val worldUid: UUID,
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float,
        val pitch: Float
) {
    fun toLocation(): Location {
        return Location(Bukkit.getWorld(worldUid), x, y, z, yaw, pitch)
    }

    companion object {
        fun new(location: Location): LocationData {
            return LocationData(location.world.uid, location.x, location.y, location.z, location.yaw, location.pitch)
        }
    }
}