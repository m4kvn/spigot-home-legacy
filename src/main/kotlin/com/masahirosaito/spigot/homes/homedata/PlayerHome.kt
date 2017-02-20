package com.masahirosaito.spigot.homes.homedata

import com.masahirosaito.spigot.homes.homedata.LocationData

data class PlayerHome(
        var defaultHome: LocationData? = null,
        val namedHomes: MutableMap<String, LocationData> = mutableMapOf()
)