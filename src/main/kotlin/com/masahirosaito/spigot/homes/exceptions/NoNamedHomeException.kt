package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.Strings
import org.bukkit.OfflinePlayer

class NoNamedHomeException(offlinePlayer: OfflinePlayer, homeName: String) :
        HomesException(Strings.NO_NAMED_HOME(offlinePlayer.name, homeName))
