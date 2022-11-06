package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.createNoHome
import org.bukkit.OfflinePlayer

class NoHomeException(offlinePlayer: OfflinePlayer) :
    HomesException(createNoHome(offlinePlayer.name))
