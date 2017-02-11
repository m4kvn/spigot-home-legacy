package com.masahirosaito.spigot.homes.homedatas

data class PlayerHomes(
        val defaultHome: DefaultHome = DefaultHome(),
        val namedHomes: MutableSet<NamedHome> = mutableSetOf()
)