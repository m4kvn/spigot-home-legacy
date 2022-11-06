package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.createNoNamedHome
import org.bukkit.OfflinePlayer

class NoNamedHomeException(offlinePlayer: OfflinePlayer, homeName: String) :
    HomesException(createNoNamedHome(offlinePlayer.name, homeName))
