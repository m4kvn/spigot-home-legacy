package com.masahirosaito.spigot.homes.homedatas

import org.bukkit.Location

data class DefaultHome(var locationData: LocationData? = null) {
    fun setLocation(location: Location) {
        locationData = LocationData.fromLocation(location)
    }
}