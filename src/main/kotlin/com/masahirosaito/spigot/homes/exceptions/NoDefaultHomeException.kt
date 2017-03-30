package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.Strings
import org.bukkit.OfflinePlayer

class NoDefaultHomeException(offlinePlayer: OfflinePlayer) :
        HomesException(Strings.NO_DEFAULT_HOME(offlinePlayer.name))
