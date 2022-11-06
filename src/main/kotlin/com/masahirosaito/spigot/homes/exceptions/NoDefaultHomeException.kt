package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.nameOrUnknown
import com.masahirosaito.spigot.homes.strings.ErrorStrings.createNoDefaultHomeMessage
import org.bukkit.OfflinePlayer

class NoDefaultHomeException(offlinePlayer: OfflinePlayer) :
    HomesException(createNoDefaultHomeMessage(offlinePlayer.nameOrUnknown))
