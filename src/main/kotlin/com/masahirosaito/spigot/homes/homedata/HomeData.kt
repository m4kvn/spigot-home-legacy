package com.masahirosaito.spigot.homes.homedata

import java.util.*

data class HomeData(
        val ownerUid: UUID,
        val name: String,
        var locationData: LocationData,
        var isPrivate: Boolean = false
) {
    fun location() = locationData.toLocation()
}
