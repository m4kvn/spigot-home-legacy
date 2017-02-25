package com.masahirosaito.spigot.homes.homedata

data class PlayerHome(
        var defaultHomeData: HomeData? = null,
        val namedHomeData: MutableMap<String, HomeData> = mutableMapOf()
)
