package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_DEFAULT_HOME
import org.bukkit.OfflinePlayer

class NoDefaultHomeException(offlinePlayer: OfflinePlayer) :
        HomesException(NO_DEFAULT_HOME(offlinePlayer.name))
