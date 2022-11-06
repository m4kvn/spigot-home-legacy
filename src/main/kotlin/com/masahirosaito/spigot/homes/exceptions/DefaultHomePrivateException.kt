package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.nameOrUnknown
import com.masahirosaito.spigot.homes.strings.ErrorStrings.createDefaultHomeIsPrivate
import org.bukkit.OfflinePlayer

class DefaultHomePrivateException(player: OfflinePlayer) :
    HomesException(createDefaultHomeIsPrivate(player.nameOrUnknown))
