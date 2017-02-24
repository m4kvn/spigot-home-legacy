package com.masahirosaito.spigot.homes.oldhomedata

import com.masahirosaito.spigot.homes.homedata.LocationData

data class OldPlayerHome(
        var defaultHome: LocationData? = null,
        val namedHomes: MutableMap<String, LocationData> = mutableMapOf()
)
