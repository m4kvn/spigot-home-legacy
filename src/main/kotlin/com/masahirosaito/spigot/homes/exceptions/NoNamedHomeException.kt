package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_NAMED_HOME
import org.bukkit.OfflinePlayer

class NoNamedHomeException(offlinePlayer: OfflinePlayer, homeName: String) :
        HomesException(NO_NAMED_HOME(offlinePlayer.name, homeName))
