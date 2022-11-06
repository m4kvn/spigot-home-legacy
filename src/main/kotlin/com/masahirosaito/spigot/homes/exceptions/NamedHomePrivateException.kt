package com.masahirosaito.spigot.homes.exceptions

import com.masahirosaito.spigot.homes.strings.ErrorStrings.createNamedHomeIsPrivate
import org.bukkit.OfflinePlayer

class NamedHomePrivateException(player: OfflinePlayer, homeName: String) :
    HomesException(createNamedHomeIsPrivate(player.name, homeName))
