package com.masahirosaito.spigot.homes.homedata

data class PlayerHome(
        var defaultHome: LocationData? = null,
        val namedHomes: MutableMap<String, LocationData> = mutableMapOf()
)